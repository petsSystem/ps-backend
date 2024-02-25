package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.model.entity.AppointmentEntityMock;
import br.com.petshop.appointment.model.map.ScheduleMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

class AppointmentScheduleServiceTest {

    @InjectMocks AppointmentScheduleService appointmentScheduleService;
    AppointmentEntity entity;
    TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> weekdayMap;
    TreeMap<DayOfWeek, Integer> weekdayAvailabilityMap;
    TreeMap<LocalDate, Boolean> monthAvailabilityMap;
    TreeMap<LocalDate, List<AppointmentEntity>> dayAppointmentsMap;
    List<AppointmentEntity> appointmentList;
    TreeMap<LocalTime, Integer> timeAvailability;
    TreeMap<LocalTime, Boolean> dayAvailability;
    TreeMap<LocalTime, List<AppointmentEntity>> hourAppointmentsMap;
    TreeMap<LocalTime, List<UUID>> hourScheduleIdsMap;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = AppointmentEntityMock.get();
        weekdayMap = ScheduleMap.weekday();
        weekdayAvailabilityMap = ScheduleMap.weekdayAvailability();
        dayAppointmentsMap = ScheduleMap.dayAppointments();
        monthAvailabilityMap = ScheduleMap.monthAvailability();
        appointmentList = ScheduleMap.getAppointmentList();
        timeAvailability = ScheduleMap.getTimeAvailability();
        dayAvailability = ScheduleMap.dayAvailability();
        hourAppointmentsMap = ScheduleMap.hourAppointmentsMap();
        hourScheduleIdsMap = ScheduleMap.hourScheduleIds();
    }

    @Test
    void testMapAppointments() {
        TreeMap<LocalDate, List<AppointmentEntity>> result = AppointmentScheduleService.mapAppointments(List.of(entity));
        Assertions.assertInstanceOf(TreeMap.class, result);
    }

    @Test
    void testGetMonthView() {
        TreeMap<LocalDate, Boolean> result = appointmentScheduleService.getMonthView(weekdayAvailabilityMap, dayAppointmentsMap);
        Assertions.assertInstanceOf(TreeMap.class, result);
    }

    @Test
    void testMapAppointmentsTime() {
        TreeMap<LocalTime, List<AppointmentEntity>> result = AppointmentScheduleService.mapAppointmentsTime(List.of(entity));
        Assertions.assertInstanceOf(TreeMap.class, result);
    }

    @Test
    void testGetDateTimeView() {
        TreeMap<LocalTime, Boolean> result = appointmentScheduleService.getDateTimeView(timeAvailability, hourAppointmentsMap);
        Assertions.assertInstanceOf(TreeMap.class, result);
    }

    @Test
    void testGetScheduleDateTimeView() {
        TreeMap<LocalTime, List<AppointmentEntity>> result = appointmentScheduleService.getScheduleDateTimeView(hourScheduleIdsMap, hourAppointmentsMap);
        Assertions.assertInstanceOf(TreeMap.class, result);
    }
}