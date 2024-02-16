package br.com.petshop.schedule.service;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.schedule.model.dto.request.ScheduleCreateRequest;
import br.com.petshop.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.schedule.model.dto.request.ScheduleUpdateRequest;
import br.com.petshop.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.schedule.model.dto.response.ScheduleTableResponse;
import br.com.petshop.schedule.model.entity.ScheduleEntity;
import br.com.petshop.schedule.model.enums.Message;
import com.github.fge.jsonpatch.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ScheduleBusinessService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(ScheduleService.class);
    @Autowired private ScheduleService service;
    @Autowired private ScheduleConverterService converter;
    @Autowired private ScheduleValidateService validate;
    public ScheduleResponse create(Principal authentication, ScheduleCreateRequest request) {
        try {
            //converte request em entidade
            ScheduleEntity entityRequest = converter.createRequestIntoEntity(request);

            //cria a entidade schedule
            ScheduleEntity entity = service.create(entityRequest);

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
            entity = service.updateById(entity);

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

    public ScheduleResponse activate (Principal authentication, UUID scheduleId, JsonPatch patch) {
        try {
            //recupera o loja pelo id
            ScheduleEntity entity = service.findById(scheduleId);

            //ativa/desativa loja
            entity = service.activate(entity, patch);

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

    public List<ScheduleTableResponse> getByFilter(Principal authentication, ScheduleFilterRequest filter ) {
        try {
            //recupera todas as agendas pelo filtro
            List<ScheduleEntity> entities = service.findAllByFilter(filter);

            //converte a entidade na resposta final
            List<ScheduleTableResponse> response = entities.stream()
                    .map(c -> converter.entityIntoTableResponse(c))
                    .collect(Collectors.toList());

            //ordena por status
            Collections.sort(response, Comparator.comparing(ScheduleTableResponse::getActive).reversed());

            return response;

        } catch (Exception ex) {
            log.error(Message.SCHEDULE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_GET_ERROR.get(), ex);
        }
    }

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
}
