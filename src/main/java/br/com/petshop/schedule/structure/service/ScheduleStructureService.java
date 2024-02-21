package br.com.petshop.schedule.structure.service;

import br.com.petshop.appointment.service.appTeste.objtest;
import br.com.petshop.commons.exception.GenericNotFoundException;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScheduleStructureService {
    @Autowired private ScheduleStructureRepository repository;
    @Autowired private ScheduleService scheduleService;
    @Autowired private ProductBusinessService productService;
    @Autowired private ScheduleStructureConverterService converter;

    /**
     * Create a structure to an individual schedule
     * @param authentication
     * @param entity
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

        List<Structure> structures = converter.mapIntoList(mapSchedule);
        entity.setStructure(structures);

        scheduleService.update(entity);
    }

    /**
     * Create a structure for a productId (which means that the result is a merge of a lot of schedules)
     * @param authentication
     * @param entity
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
     * Return all the products of a schedule
     * @param authentication
     * @param entity
     * @return
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
     * Return all schedules for a productId
     * @param companyId
     * @param productId
     * @return
     */
    private List<ScheduleEntity> getAllSchedulesByProductId(UUID companyId, UUID productId) {
        ScheduleFilterRequest filter = ScheduleFilterRequest.builder()
                .companyId(companyId)
                .productId(productId)
                .build();
        return scheduleService.findAllByFilter(filter);
    }

    /**
     * Create a schedule structure for one schedule or a lot schedules by productId
     * @param intervalMinutes
     * @param schedules
     * @return
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
     * Return all Times for weekday to a schedule. The structure include the id of a schedule into the time.
     * @param dow
     * @param scheduleId
     * @param intervalMinutes
     * @return
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
     * Save a structure of schedules with the same productId.
     * @param productId
     * @param mapSchedule
     * @return
     */
    private ScheduleStructureEntity save(UUID productId,  TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> mapSchedule) {
        ScheduleStructureEntity structure = findByProductId(productId);

        if (structure == null) {
            structure = ScheduleStructureEntity.builder().build();
            structure.setProductId(productId);
        }

        List<Structure> structureList = converter.mapIntoList(mapSchedule);
        structure.setStructure(structureList);

        return repository.save(structure);
    }

    /**
     * Find structure for a productId
     * @param productId
     * @return
     */
    public ScheduleStructureEntity findByProductId(UUID productId) {
        Optional<ScheduleStructureEntity> entity = repository.findByProductId(productId);
        if (entity.isEmpty())
            return null;

        return entity.get();
    }

    /**
     * Returns a number of available appointments for a specific time of a structure.
     * @param structure
     * @return
     */
    public TreeMap<DayOfWeek, Integer> getAvailability(TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> structure) {
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
     * Returns a structure map.
     * @param structures
     * @return
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
