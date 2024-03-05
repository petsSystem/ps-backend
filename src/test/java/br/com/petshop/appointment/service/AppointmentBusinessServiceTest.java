package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.dto.request.AppointmentCreateRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentCreateRequestMock;
import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequestMock;
import br.com.petshop.appointment.model.dto.request.AppointmentStatusRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentStatusRequestMock;
import br.com.petshop.appointment.model.dto.request.AppointmentUpdateRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentUpdateRequestMock;
import br.com.petshop.appointment.model.dto.response.AppointmentResponse;
import br.com.petshop.appointment.model.dto.response.AppointmentResponseMock;
import br.com.petshop.appointment.model.dto.response.AppointmentTableResponse;
import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.model.entity.AppointmentEntityMock;
import br.com.petshop.appointment.model.map.ScheduleMap;
import br.com.petshop.schedule.service.ScheduleBusinessService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AppointmentBusinessServiceTest {
    @Mock Principal authentication;
    @Mock AppointmentService service;
    @Mock AppointmentConverterService converter;
    @Mock ScheduleBusinessService scheduleService;
    @Mock AppointmentScheduleService appointmentScheduleService;
    @InjectMocks AppointmentBusinessService appointmentBusinessService;
    AppointmentEntity entity;
    AppointmentFilterRequest filter;
    AppointmentCreateRequest createRequest;
    AppointmentUpdateRequest updateRequest;
    AppointmentStatusRequest statusRequest;
    AppointmentResponse response;
    TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> weekdayMap;
    TreeMap<DayOfWeek, Integer> weekdayAvailabilityMap;
    TreeMap<LocalDate, Boolean> monthAvailabilityMap;
    TreeMap<LocalDate, List<AppointmentEntity>> dayAppointmentsMap;
    List<AppointmentEntity> appointmentList;
    TreeMap<LocalTime, Integer> timeAvailability;
    TreeMap<LocalTime, Boolean> dayAvailability;
    TreeMap<LocalTime, List<AppointmentEntity>> hourAppointmentsMap;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = AppointmentEntityMock.get();
        filter = AppointmentFilterRequestMock.get();
        createRequest = AppointmentCreateRequestMock.get();
        updateRequest = AppointmentUpdateRequestMock.get();
        statusRequest = AppointmentStatusRequestMock.get();
        response = AppointmentResponseMock.get();
        weekdayMap = ScheduleMap.weekday();
        weekdayAvailabilityMap = ScheduleMap.weekdayAvailability();
        dayAppointmentsMap = ScheduleMap.dayAppointments();
        monthAvailabilityMap = ScheduleMap.monthAvailability();
        appointmentList = ScheduleMap.getAppointmentList();
        timeAvailability = ScheduleMap.getTimeAvailability();
        dayAvailability = ScheduleMap.dayAvailability();
        hourAppointmentsMap = ScheduleMap.hourAppointmentsMap();
    }

    @Test
    void testCreate() {
        when(service.create(any())).thenReturn(entity);
        when(converter.createRequestIntoEntity(any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        AppointmentResponse result = appointmentBusinessService.create(authentication, createRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testUpdateById() {
        when(service.updateById(any())).thenReturn(entity);
        when(service.findById(any())).thenReturn(entity);
        when(converter.updateRequestIntoEntity(any())).thenReturn(entity);
        when(converter.updateRequestIntoEntity(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        AppointmentResponse result = appointmentBusinessService.updateById(authentication, UUID.randomUUID(), updateRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testSetStatus() {
        when(service.findById(any())).thenReturn(entity);
        when(service.setStatus(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        AppointmentResponse result = appointmentBusinessService.setStatus(authentication, statusRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testGetMonthAvailability() {
        when(service.findAllByFilter(any())).thenReturn(List.of(entity));
        when(scheduleService.getStructure(any(), any())).thenReturn(weekdayMap);
        when(scheduleService.getWeekDayAvailability(any())).thenReturn(weekdayAvailabilityMap);
        when(appointmentScheduleService.getMonthView(any(), any())).thenReturn(monthAvailabilityMap);

        TreeMap<LocalDate, Boolean> result = appointmentBusinessService.getMonthAvailability(authentication, filter);
        Assertions.assertEquals(monthAvailabilityMap, result);
    }

    @Test
    void testGetDayAvailability() {
        when(service.findAllByFilter(any())).thenReturn(List.of(entity));
        when(scheduleService.getStructure(any(), any())).thenReturn(weekdayMap);
        when(scheduleService.getTimeAvailability(any(), any())).thenReturn(timeAvailability);
        when(appointmentScheduleService.getDateTimeView(any(), any())).thenReturn(dayAvailability);

        TreeMap<LocalTime, Boolean> result = appointmentBusinessService.getDayAvailability(null, new AppointmentFilterRequest(null, null, null, null, LocalDate.of(2024, Month.FEBRUARY, 24)));
        Assertions.assertEquals(dayAvailability, result);
    }

    @Test
    void testSchedule() {
        when(service.findAllByFilter(any())).thenReturn(List.of(entity));
        when(scheduleService.getStructure(any(), any())).thenReturn(weekdayMap);
        when(appointmentScheduleService.getScheduleDateTimeView(any(), any())).thenReturn(hourAppointmentsMap);

        List<AppointmentTableResponse> result = appointmentBusinessService.schedule(authentication, new AppointmentFilterRequest(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.now()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testGetById() {
        when(service.findById(any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        AppointmentResponse result = appointmentBusinessService.getById(authentication, UUID.randomUUID());
        Assertions.assertEquals(response, result);
    }
}