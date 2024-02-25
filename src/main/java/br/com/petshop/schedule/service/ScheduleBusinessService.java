package br.com.petshop.schedule.service;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.schedule.model.dto.Structure;
import br.com.petshop.schedule.model.dto.request.ScheduleCreateRequest;
import br.com.petshop.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.schedule.model.dto.request.ScheduleUpdateRequest;
import br.com.petshop.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.schedule.model.entity.ScheduleEntity;
import br.com.petshop.schedule.model.enums.Message;
import br.com.petshop.schedule.structure.model.entity.ScheduleStructureEntity;
import br.com.petshop.schedule.structure.service.ScheduleStructureService;
import com.github.fge.jsonpatch.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe responsável pelas regras de negócio da agenda
 */
@Service
public class ScheduleBusinessService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(ScheduleService.class);
    @Autowired private ScheduleService service;
    @Autowired private ScheduleConverterService converter;
    @Autowired private ScheduleValidateService validate;
    @Autowired private ScheduleStructureService structureService;

    /**
     * Método de criação de agenda.
     * @param authentication - dados do usuário logado
     * @param request - dto contendo dados de criação da agenda
     * @return - dados da agenda
     */
    public ScheduleResponse create(Principal authentication, ScheduleCreateRequest request) {
        try {
            //converte request em entidade
            ScheduleEntity entityRequest = converter.createRequestIntoEntity(request);

            //cria a entidade schedule
            ScheduleEntity entity = service.create(entityRequest);

            //atualizo a estrutura de disponibilidade da agenda
            structureService.updateIndividualStructure(authentication, entity);

            //atualizo a estrutura de disponibilidade dos serviços
            structureService.updateStructure(authentication, entity);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error(Message.SCHEDULE_ALREADY_REGISTERED_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.SCHEDULE_ALREADY_REGISTERED_ERROR.get(), ex);
        } catch (GenericNotFoundException ex) {
            log.error(Message.SCHEDULE_USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.SCHEDULE_USER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SCHEDULE_CREATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_CREATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de atualização de agenda.
     * @param authentication - dados do usuário logado
     * @param scheduleId - id de cadastro da agenda
     * @param request - dto contendo dados de atualização da agenda
     * @return - dados da agenda
     */
    public ScheduleResponse updateById(Principal authentication, UUID scheduleId, ScheduleUpdateRequest request) {
        try {
            //recupera a agenda ativa pelo id
            ScheduleEntity entity = service.findById(scheduleId);

            //valida acesso a loja
            validate.accessByCompany(authentication, request.getCompanyId());

            //converte request em entidade
            ScheduleEntity entityRequest = converter.updateRequestIntoEntity(request);
            entity = converter.updateRequestIntoEntity(entityRequest, entity);

            //faz atualizaçao da entidade
            entity = service.update(entity);

            //atualizo a estrutura de disponibilidade da agenda
            structureService.updateIndividualStructure(authentication, entity);

            //atualizo a estrutura de disponibilidade dos serviços
            structureService.updateStructure(authentication, entity);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.SCHEDULE_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.SCHEDULE_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SCHEDULE_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_UPDATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de ativação/desativação de agenda.
     * @param authentication - dados do usuário logado
     * @param scheduleId - id de cadastro da agenda
     * @param patch - dados de ativação/desativação da agenda
     * @return - dados da agenda
     */
    public ScheduleResponse activate (Principal authentication, UUID scheduleId, JsonPatch patch) {
        try {
            //recupera o loja pelo id
            ScheduleEntity entity = service.findById(scheduleId);

            //ativa/desativa loja
            entity = service.activate(entity, patch);

            //atualizo a estrutura de disponibilidade dos serviços
            structureService.updateStructure(authentication, entity);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.SCHEDULE_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.SCHEDULE_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SCHEDULE_ACTIVATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_ACTIVATE_ERROR.get(), ex);
        }
    }

    /**
     * Método que retorna dados de agendas, de acordo com filtro informado.
     * @param authentication - dados do usuário logado
     * @param filter - companyId, categoryId, userId, productId
     * @return - lista de dados da agenda
     */
    public List<ScheduleResponse> getByFilter(Principal authentication, ScheduleFilterRequest filter ) {
        try {
            //recupera todas as agendas pelo filtro
            List<ScheduleEntity> entities = service.findAllByFilter(filter);

            //converte a entidade na resposta final
            return entities.stream()
                    .map(c -> converter.entityIntoResponse(c))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error(Message.SCHEDULE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método que retorna uma agenda, através da informação do id.
     * @param authentication - dados do usuário logado
     * @param scheduleId - id de cadastro da agenda
     * @return - dados da agenda
     */
    public ScheduleResponse getById(Principal authentication, UUID scheduleId) {
        try {
            //busca agenda pelo id
            ScheduleEntity entity = service.findById(scheduleId);

            //converte entidade para a resposta
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.SCHEDULE_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.SCHEDULE_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SCHEDULE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método que retorna a estrutura de agenda/agendamento de um ou mais usuários do sistema.
     * @param userId - id de cadastro do usuário
     * @param productId - id de cadastro do produto/serviço
     * @return - árvore de data da semana x árvore de horários e lista de ids de agendas
     */
    public TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> getStructure(UUID userId, UUID productId) {
        try {
            List<Structure> structures = new ArrayList<>();
            if (userId != null) { //recupero a estrutura de uma agenda
                ScheduleEntity entity = service.findByUserId(userId);
                structures = entity.getStructure();
            } else { //recupero a estrutura de um produto na tabela schedule_structure
                ScheduleStructureEntity entity = structureService.findByProductId(productId);
                if (entity == null) throw new GenericNotFoundException();
                structures = entity.getStructure();
            }
            return structureService.getStructureMap(structures);

        } catch (GenericNotFoundException ex) {
            log.error(Message.SCHEDULE_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.SCHEDULE_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SCHEDULE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método que retorna os dias do mês disponíveis (cálculo feito para 30 dias).
     * @param structure - estrutura de agendamento
     * @return - árvore de data x quantidade possível de agendamento
     */
    public TreeMap<DayOfWeek, Integer> getWeekDayAvailability(TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> structure) {
        try {
            return structureService.getWeekDayAvailability(structure);

        } catch (GenericNotFoundException ex) {
            log.error(Message.SCHEDULE_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.SCHEDULE_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SCHEDULE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método que retorna os horários disponíveis de uma determinada data.
     * @param date - data do agendamento
     * @param structure - estrutura de agendamento
     * @return - árvore de horário x quantidade possível de agendamento
     */
    public TreeMap<LocalTime, Integer> getTimeAvailability(LocalDate date, TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> structure) {
        try {
            return structureService.getTimeAvailability(date, structure);

        } catch (GenericNotFoundException ex) {
            log.error(Message.SCHEDULE_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.SCHEDULE_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SCHEDULE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_GET_ERROR.get(), ex);
        }
    }
}
