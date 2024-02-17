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
import br.com.petshop.product.model.dto.response.ProductResponse;
import br.com.petshop.product.service.ProductBusinessService;
import br.com.petshop.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.schedule.service.ScheduleBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
    @Autowired private ProductBusinessService productService;
    @Autowired private AppointmentBusinessUtils utils;
    @Autowired private ScheduleBusinessService scheduleService;

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

    public Map<String, List<AppointmentResponse>> getByFilter(Principal authentication, AppointmentFilterRequest filter ) {
        try {
            //recuperar o product
            ProductResponse product = productService.getById(authentication, filter.getProductId());

            //recuperar as agendas por productId e/ou userId
            List<ScheduleResponse> schedules = scheduleService.getByFilter(authentication, utils.getScheduleFilter(filter));

            //criar map com mes/ano
            Map<String, List<AppointmentResponse>> scheduleMap = utils.createMap(product, schedules);

            //para cada scheduleId
            for (ScheduleResponse schedule : schedules) {
                //recuperar os appointments
                List<AppointmentEntity> appointments  = service.findByScheduleId(schedule.getId());
                //para cada appointment
                for (AppointmentEntity appointment : appointments) {
                    //verificar mes/ano e incluir na lista do map
                    String key = utils.getKey(appointment.getDate());

                    AppointmentResponse response = converter.entityIntoResponse(appointment);
                    scheduleMap.get(key).add(response);

//                    List<AppointmentResponse> apps = new ArrayList<>();
//                    apps.addAll(map.get(app.getDate()));
//                    apps.add(app);
//                    map.put(app.getDate(), apps);

                }
            }


            //arrumar o find do appointment... hoje ele pega todos do scheudle id.
            //precisarei pegar por query... pegar todos os appointments do schedule id cujo date
            //seja maior que a data atual

            return scheduleMap;




//
//
//            List<AppointmentEntity> entities = service.findAllByFilter(filter);
//
//            return entities.stream()
//                    .map(c -> converter.entityIntoResponse(c))
//                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error(Message.APPOINTMENT_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.APPOINTMENT_GET_ERROR.get(), ex);
        }
    }

//    public Map<LocalDate, List<AppointmentResponse>> getByScheduleId(UUID scheduleId) {
//
//        List<AppointmentEntity> entities = service.findByScheduleId(scheduleId);
//        List<AppointmentResponse> appointments = entities.stream()
//                .map(c -> converter.entityIntoResponse(c))
//                .collect(Collectors.toList());
//
//        Collections.sort(appointments, Comparator.comparing(AppointmentResponse::getDate));
//
//        Map<LocalDate, List<AppointmentResponse>> map = new HashMap<>();
//
//        for (AppointmentResponse app : appointments) {
//            if (map.isEmpty() || map.get(app.getDate()) == null) {
//                map.put(app.getDate(), List.of(app));
//            } else {
//                List<AppointmentResponse> apps = new ArrayList<>();
//                apps.addAll(map.get(app.getDate()));
//                apps.add(app);
//                map.put(app.getDate(), apps);
//            }
//        }
//
//        return map;
//
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
