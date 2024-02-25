package br.com.petshop.schedule.structure.service;

import br.com.petshop.commons.model.Day;
import br.com.petshop.product.model.dto.response.ProductResponse;
import br.com.petshop.product.service.ProductBusinessService;
import br.com.petshop.schedule.model.dto.Structure;
import br.com.petshop.schedule.model.dto.Times;
import br.com.petshop.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.schedule.model.entity.ScheduleEntity;
import br.com.petshop.schedule.service.ScheduleService;
import br.com.petshop.schedule.structure.model.entity.ScheduleStructureEntity;
import br.com.petshop.schedule.structure.repository.ScheduleStructureRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classe de serviços para gerenciamento de estrutura de agendas x agendamentos
 */
@Service
public class ScheduleStructureService {
    @Autowired private ScheduleStructureRepository repository;
    @Autowired private ScheduleService scheduleService;
    @Autowired private ProductBusinessService productService;
    @Autowired private ScheduleStructureConverterService converter;

    /**
     * Salva uma estrutura de agendas x agendamentos para uma única agenda informadda.
     * @param authentication - dados do usuário logado
     * @param entity - entidade de agenda
     */
    public void updateIndividualStructure (Principal authentication, ScheduleEntity entity) {

        //recupero todos os produtos ativos e principais
        List<ProductResponse> products = getAllProducts(authentication, entity);

        //recuperar o maior intervalMinutes
        Integer intervalMinutes = 0;
        for (ProductResponse product : products) {
            if (intervalMinutes < product.getIntervalMinutes())
                intervalMinutes = product.getIntervalMinutes();
        }

        //setar a agenda na lista
        List<ScheduleEntity> schedules = List.of(entity);

        //crio estrutura (mergeada) de disponibilidade das agendas do mesmo produto
        TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> mapSchedule =
                createScheduleStructure(intervalMinutes, schedules);

        List<Structure> structures = converter.mapToList(mapSchedule);
        entity.setStructure(structures);

        scheduleService.update(entity);
    }

    /**
     * Método que atualiza a estrutura de agenda x agendamentos de um id de produto/serviço.
     * @param authentication - dados do usuário logado
     * @param entity - entidade de agenda
     */
    public void updateStructure(Principal authentication, ScheduleEntity entity) {

        //recupero todos os produtos ativos e principais
        List<ProductResponse> products = getAllProducts(authentication, entity);

        //para cada produto
        for (ProductResponse product : products) {

            //recuperar todas as agendas que atendem esse produto
            List<ScheduleEntity> schedules = getAllSchedulesByProductId(entity.getCompanyId(), product.getId());

            //crio estrutura (mergeada) de disponibilidade das agendas do mesmo produto
            TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> structure =
                    createScheduleStructure(product.getIntervalMinutes(), schedules);

            //salva estrutura
            save(product.getId(), structure);
        }
    }

    /**
     * Método que retorna os produtos de uma agenda.
     * @param authentication - dados do usuário logado
     * @param entity - entidade de agenda
     * @return - lista de entidade de produto/serviço
     */
    private List<ProductResponse> getAllProducts(Principal authentication, ScheduleEntity entity) {
        List<ProductResponse> products = new ArrayList<>();
        for(UUID productId : entity.getProductIds()) {
            ProductResponse product = productService.getById(authentication, productId);
            if (product.getActive() && !product.getAdditional()) {
                products.add(product);
            }
        }
        return products;
    }

    /**
     * Método que retorna todas as agendas de um id de produto/serviço.
     * @param companyId - id de cadastro de uma loja/petshop
     * @param productId - id de cadastro de um produto/serviço
     * @return - lista de entidades de estrutura de agendas x agendamentos
     */
    private List<ScheduleEntity> getAllSchedulesByProductId(UUID companyId, UUID productId) {
        ScheduleFilterRequest filter = ScheduleFilterRequest.builder()
                .companyId(companyId)
                .productId(productId)
                .build();
        return scheduleService.findAllByFilter(filter);
    }

    /**
     * Método que cria uma estrutura de ou mais agendas para um id de produto/serviço.
     * @param intervalMinutes - intervalo em minutos do produto/serviço
     * @param schedules - lista de entidades de estrutura de agendas x agendamentos
     * @return - árvore dia da semana x (árvore horário x lista de ids de agendamentos)
     */
    private TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> createScheduleStructure(Integer intervalMinutes, List<ScheduleEntity> schedules) {

        TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> mapSchedule = new TreeMap<>();
        TreeMap<LocalTime, List<UUID>> map1 = new TreeMap<>();

        for (ScheduleEntity sch : schedules) {
            System.out.println(sch.getId() + " - " + sch.getName());
            for (Day dow : sch.getDays()) {
                map1 = mapSchedule.get(dow.getWeekday());
                TreeMap<LocalTime, List<UUID>> scheduleTime = setScheduleTime(dow, sch.getId(), intervalMinutes);

                if (map1 == null)
                    mapSchedule.put(dow.getWeekday(), scheduleTime);
                else {
                    Map<LocalTime, List<UUID>> map2 = scheduleTime;
                    map1 = Stream.concat(map1.entrySet().stream(), map2.entrySet().stream())
                            .collect(Collectors.groupingBy(e-> e.getKey(),
                                    TreeMap::new,
                                    Collectors.flatMapping(e -> e.getValue().stream(), Collectors.toList())));

                    mapSchedule.put(dow.getWeekday(), map1);
                }
            }
        }
        return mapSchedule;
    }

    /**
     * Método que recupera todos os horários dos dias da semana para uma agenda.
     * A estrutura inclui o id de uma agenda no horário.
     * @param dow - entidade dia que contém o dia da semana, horário inicial e final de trabalho
     * @param scheduleId - id de cadastro da agenda
     * @param intervalMinutes - intervalo em minutos do produto/serviço
     * @return - árvore de horário x lista de ids de agendamentos
     */
    public TreeMap<LocalTime, List<UUID>> setScheduleTime(Day dow, UUID scheduleId, Integer intervalMinutes) {
        TreeMap<LocalTime, List<UUID>> scheduleTime = new TreeMap<>();

        LocalTime initial = LocalTime.parse(dow.getInitialTime());
        LocalTime end = LocalTime.parse(dow.getEndTime());
        while(initial.isBefore(end)) {
            scheduleTime.put(initial, List.of(scheduleId));
            initial = initial.plusMinutes(intervalMinutes);
        }
        return scheduleTime;
    }

    /**
     * Salve uma estrutura de agendamentos com o mesmo productId.
     * @param productId - id de cadastro do produto/serviço
     * @param mapSchedule - árvore dia da semana x (árvore horário x lista de ids de agendamentos)
     * @return - entidade de estrutura de agendas x agendamentos
     */
    private ScheduleStructureEntity save(UUID productId,  TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> mapSchedule) {
        ScheduleStructureEntity structure = findByProductId(productId);

        if (structure == null) {
            structure = ScheduleStructureEntity.builder().build();
            structure.setProductId(productId);
        }

        List<Structure> structureList = converter.mapToList(mapSchedule);
        structure.setStructure(structureList);

        return repository.save(structure);
    }

    /**
     * Método que recupera uma entidade de estrutura através da infomração do id do produto/serviço.
     * @param productId - id de cadastro do produto/serviço
     * @return - entidade de estrutura de agendas x agendamentos
     */
    public ScheduleStructureEntity findByProductId(UUID productId) {
        Optional<ScheduleStructureEntity> entity = repository.findByProductId(productId);
        if (entity.isEmpty())
            return null;

        return entity.get();
    }

    /**
     * Método que retorna um número de agendamentos disponíveis para um dia específico da semana
     * de uma estrutura.
     * @param structure - árvore dia da semana x (árvore horário x lista de ids de agendamentos)
     * @return - árvore de dia da semana x quantidade de horários disponíveis
     */
    public TreeMap<DayOfWeek, Integer> getWeekDayAvailability(TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> structure) {
        TreeMap<DayOfWeek, Integer> available = new TreeMap<>();

        for (DayOfWeek dow : structure.keySet()) {
            TreeMap<LocalTime, List<UUID>> times = structure.get(dow);
            Integer count = available.get(dow);
            if (count == null)
                count = 0;
            count = count + times.size();
            available.put(dow, count);
        }
        return available;
    }

    /**
     * Método que retorna um número de agendamentos disponíveis para um horário específico do
     * dia de uma estrutura.
     * @param date - data selecionada para agendamento
     * @param structure - árvore dia da semana x (árvore horário x lista de ids de agendamentos)
     * @return - árvore de horário x quantidade de vagas disponíveis
     */
    public TreeMap<LocalTime, Integer> getTimeAvailability(LocalDate date, TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> structure) {
        TreeMap<LocalTime, Integer> available = new TreeMap<>();
        TreeMap<LocalTime, List<UUID>> times = structure.get(date.getDayOfWeek());

        for (LocalTime time : times.keySet()) {
            List<UUID> appointmentIds = times.get(time);
            Integer count = available.get(time);
            if (count == null)
                count = 0;
            count = count + appointmentIds.size();
            available.put(time, count);
        }
        return available;
    }

    /**
     * Método que transforma uma lista de estrutura de agendas x agendamentos em um map de
     * dia da semana x (árvore horário x lista de ids de agendamentos)
     * @param structures - lista de estrutura de agenda x agendamento
     * @return - árvore dia da semana x (árvore horário x lista de ids de agendamentos)
     * @throws JsonProcessingException
     */
    public TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> getStructureMap(List<Structure> structures) throws JsonProcessingException {
        TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> structureMap = new TreeMap<>();

        ObjectMapper map = new ObjectMapper();
        map.registerModule(new JavaTimeModule());
        map.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        for (Structure str : structures) {
            List<String> times = str.getTimes();
            List<Times> tms = map.readValue(times.toString(), new TypeReference<List<Times>>(){});

            TreeMap<LocalTime, List<UUID>> timeMap = new TreeMap<>();
            for (Times t : tms)
                timeMap.put(t.getTime(), t.getScheduleIds());

            structureMap.put(str.getWeekday(), timeMap);
        }
        return structureMap;
    }
}
