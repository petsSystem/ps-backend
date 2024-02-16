package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.dto.request.AppointmentCreateRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentStatusRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentUpdateRequest;
import br.com.petshop.appointment.model.dto.response.AppointmentResponse;
import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.model.enums.Message;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.service.AuthenticationCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AppointmentBusinessService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(AppointmentService.class);
    @Autowired private AppointmentService service;
    @Autowired private AppointmentConverterService converter;

    public AppointmentResponse create(Principal authentication, AppointmentCreateRequest request) {
        try {
            //converte request em entidade
            AppointmentEntity entityRequest = converter.createRequestIntoEntity(request);

            //cria a entidade
            AppointmentEntity entity = service.create(entityRequest);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (Exception ex) {
            log.error(Message.APPOINTMENT_CREATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.APPOINTMENT_CREATE_ERROR.get(), ex);
        }
    }

    public AppointmentResponse updateById(Principal authentication, UUID appointmentId, AppointmentUpdateRequest request) {
        try {
            //recupera o agendamento pelo id
            AppointmentEntity entity = service.findById(appointmentId);

            //converte request em entidade
            AppointmentEntity entityRequest = converter.updateRequestIntoEntity(request);
            entity = converter.updateRequestIntoEntity(entityRequest, entity);

            //faz atualizaçao da entidade
            entity = service.updateById(entity);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.APPOINTMENT_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.APPOINTMENT_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.APPOINTMENT_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.APPOINTMENT_UPDATE_ERROR.get(), ex);
        }
    }

    public AppointmentResponse setStatus(Principal authentication, AppointmentStatusRequest request) {
        try {
            //recupera appointment pelo id
            AppointmentEntity entity = service.findById(request.getAppointmentId());

            //faz a atualização do status
            entity = service.setStatus(entity, request);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.APPOINTMENT_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.APPOINTMENT_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.APPOINTMENT_STATUS_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.APPOINTMENT_STATUS_ERROR.get(), ex);
        }
    }

    public List<AppointmentResponse> getByFilter(AppointmentFilterRequest filter ) {
        try {
            List<AppointmentEntity> entities = service.findAllByFilter(filter);

            return entities.stream()
                    .map(c -> converter.entityIntoResponse(c))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error(Message.APPOINTMENT_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.APPOINTMENT_GET_ERROR.get(), ex);
        }
    }

    public Map<LocalDate, AppointmentResponse> getByScheduleId(UUID scheduleId) {

        List<AppointmentEntity> entities = service.findByScheduleId(scheduleId);
        List<AppointmentResponse> appointments = entities.stream()
                .map(c -> converter.entityIntoResponse(c))
                .collect(Collectors.toList());

        Collections.sort(appointments, Comparator.comparing(AppointmentResponse::getDate));

        Map<LocalDate, AppointmentResponse> map = appointments.stream()
                .collect(Collectors.toMap(AppointmentResponse::getDate, Function.identity()));

        return map;

    }

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
