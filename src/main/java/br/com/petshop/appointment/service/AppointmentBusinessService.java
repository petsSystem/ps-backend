package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.dto.request.AppointmentCreateRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentUpdateRequest;
import br.com.petshop.appointment.model.dto.response.AppointmentResponse;
import br.com.petshop.appointment.model.dto.response.AppointmentTableResponse;
import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.model.enums.Message;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.service.AuthenticationCommonService;
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
public class AppointmentBusinessService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(AppointmentService.class);
    @Autowired private AppointmentService service;
    @Autowired private AppointmentConverterService converter;

//    public AppointmentResponse create(Principal authentication, AppointmentCreateRequest request) {
//        try {
//
//            AppointmentEntity entityRequest = converter.createRequestIntoEntity(request);
//
//            AppointmentEntity entity = service.create(entityRequest);
//
//            service.save(entity);
//
//            return converter.entityIntoResponse(entity);
//
//        } catch (GenericAlreadyRegisteredException ex) {
//            log.error(Message.SCHEDULE_ALREADY_REGISTERED_ERROR.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.UNPROCESSABLE_ENTITY, Message.SCHEDULE_ALREADY_REGISTERED_ERROR.get(), ex);
//        } catch (GenericNotFoundException ex) {
//            log.error(Message.SCHEDULE_USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.UNPROCESSABLE_ENTITY, Message.SCHEDULE_USER_NOT_FOUND_ERROR.get(), ex);
//        } catch (Exception ex) {
//            log.error(Message.SCHEDULE_CREATE_ERROR.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_CREATE_ERROR.get(), ex);
//        }
//    }
//
//    public AppointmentResponse activate (Principal authentication, UUID scheduleId, JsonPatch patch) {
//        try {
//
//            AppointmentEntity entity = service.activate(scheduleId, patch);
//
//            return  converter.entityIntoResponse(entity);
//
//        } catch (GenericNotFoundException ex) {
//            log.error(Message.SCHEDULE_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, Message.SCHEDULE_NOT_FOUND_ERROR.get(), ex);
//        } catch (Exception ex) {
//            log.error(Message.SCHEDULE_ACTIVATE_ERROR.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_ACTIVATE_ERROR.get(), ex);
//        }
//    }
//
//    public AppointmentResponse updateById(Principal authentication, UUID scheduleId, AppointmentUpdateRequest request) {
//        try {
//
//            AppointmentEntity entityRequest = converter.updateRequestIntoEntity(request);
//
//            AppointmentEntity entity = service.updateById(scheduleId, entityRequest);
//
//            return converter.entityIntoResponse(entity);
//
//        } catch (GenericNotFoundException ex) {
//            log.error(Message.SCHEDULE_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, Message.SCHEDULE_NOT_FOUND_ERROR.get(), ex);
//        } catch (Exception ex) {
//            log.error(Message.SCHEDULE_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_UPDATE_ERROR.get(), ex);
//        }
//    }
//
//    public List<AppointmentTableResponse> getByProductId(Principal authentication, UUID companyId) {
//        try {
//            List<AppointmentEntity> entities = service.findAllByProductId(companyId);
//
//            List<AppointmentTableResponse> response = entities.stream()
//                    .map(c -> converter.entityIntoTableResponse(c))
//                    .collect(Collectors.toList());
//
//            Collections.sort(response, Comparator.comparing(AppointmentTableResponse::getActive).reversed());
//
//            return response;
//
//        } catch (Exception ex) {
//            log.error(Message.SCHEDULE_GET_ERROR.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_GET_ERROR.get(), ex);
//        }
//    }
//
//    public AppointmentResponse getById(Principal authentication, UUID scheduleId) {
//        try {
//            AppointmentEntity entity = service.findById(scheduleId);
//            return converter.entityIntoResponse(entity);
//
//        } catch (GenericNotFoundException ex) {
//            log.error(Message.SCHEDULE_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, Message.SCHEDULE_NOT_FOUND_ERROR.get(), ex);
//        } catch (Exception ex) {
//            log.error(Message.SCHEDULE_GET_ERROR.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, Message.SCHEDULE_GET_ERROR.get(), ex);
//        }
//    }
}
