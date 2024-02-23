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
import br.com.petshop.schedule.service.ScheduleBusinessService;
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
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

@Service
public class AppointmentBusinessService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(AppointmentService.class);
    @Autowired private AppointmentService service;
    @Autowired private AppointmentConverterService converter;
    @Autowired private ScheduleBusinessService scheduleService;
    @Autowired private AppointmentScheduleService appointmentScheduleService;

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

    public TreeMap<LocalDate, Boolean> getMonthAvailability(Principal authentication, AppointmentFilterRequest filter) {
        try {
            //recupero a estrutura do agendamento
            TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> structure =
                    scheduleService.getStructure(filter.getUserId(), filter.getProductId());

            //recupero a quantidade de agenda disponivel por dia da semana
            TreeMap<DayOfWeek, Integer> structureAvailability = scheduleService.getWeekDayAvailability(structure);

            //recupera agendamentos pelo companyId, productId, userId (opcional)
            List<AppointmentEntity> appointments = service.findAllByFilter(filter);

            //transformar os agendamentos em Map<Data, Agendamento>
            TreeMap<LocalDate, List<AppointmentEntity>> appointmentsMap =
                    appointmentScheduleService.mapAppointments(appointments);

            //MERGE DE DISPONIBILIDADE DE AGENDA (visualização mensal) - fixo 3 meses
            return appointmentScheduleService.getMonthView(structureAvailability, appointmentsMap);

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

    public TreeMap<LocalTime, Boolean> getDayAvailability(Principal authentication, AppointmentFilterRequest filter) {
        try {
            //recupero a estrutura do agendamento completo
            TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> structure =
                    scheduleService.getStructure(filter.getUserId(), filter.getProductId());

            //recupero a quantidade de agenda disponivel por horario do dia
            TreeMap<LocalTime, Integer> structureTimeAvailability = scheduleService.getTimeAvailability(filter.getDate(), structure);

            //recupera agendamentos pelo companyId, productId, userId (opcional) e date (opcional)
            List<AppointmentEntity> appointments = service.findAllByFilter(filter);

            //transformar os agendamentos em Map<Time, Agendamento>
            TreeMap<LocalTime, List<AppointmentEntity>> appointmentsTimeMap =
                    appointmentScheduleService.mapAppointmentsTime(appointments);

            //MERGE DE DISPONIBILIDADE DE AGENDA (visualização dia)
            return appointmentScheduleService.getDateTimeView(structureTimeAvailability, appointmentsTimeMap);

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

    public TreeMap<LocalTime, List<AppointmentEntity>> schedule(Principal authentication, AppointmentFilterRequest filter) {
        try {
            //recupero a estrutura do agendamento completo
            TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> structure =
                    scheduleService.getStructure(filter.getUserId(), filter.getProductId());

            //recupero os horários da estrutura da agenda com seus respectivos agendamentos
            TreeMap<LocalTime, List<UUID>> structureTime = structure.get(filter.getDate().getDayOfWeek());

            //recupera agendamentos pelo companyId, productId, userId (opcional) e date (opcional)
            List<AppointmentEntity> appointments = service.findAllByFilter(filter);

            //transformar os agendamentos em Map<Time, Agendamento>
            TreeMap<LocalTime, List<AppointmentEntity>> appointmentsTimeMap =
                    appointmentScheduleService.mapAppointmentsTime(appointments);

            //MERGE DE AGENDAMENTOS DE AGENDA (visualização dia)
            return appointmentScheduleService.getScheduleDateTimeView(structureTime, appointmentsTimeMap);

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

    public AppointmentResponse getById(Principal authentication, UUID appointmentId) {
        try {
            AppointmentEntity entity = service.findById(appointmentId);
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.APPOINTMENT_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.APPOINTMENT_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.APPOINTMENT_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.APPOINTMENT_GET_ERROR.get(), ex);
        }
    }
}
