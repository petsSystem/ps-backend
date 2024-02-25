package br.com.petshop.appointment.model.map;

import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.model.entity.AppointmentEntityMock;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

public class ScheduleMap {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> weekday() {
        TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> map = new TreeMap<>();

        TreeMap<LocalTime, List<UUID>> mapTime = new TreeMap<>();
        mapTime.put(LocalTime.parse("09:00"), List.of(UUID.randomUUID()));
        mapTime.put(LocalTime.parse("09:30"), List.of(UUID.randomUUID()));
        mapTime.put(LocalTime.parse("10:00"), List.of(UUID.randomUUID()));
        mapTime.put(LocalTime.parse("10:30"), List.of(UUID.randomUUID()));
        mapTime.put(LocalTime.parse("11:00"), List.of(UUID.randomUUID()));
        mapTime.put(LocalTime.parse("11:30"), List.of(UUID.randomUUID()));

        map.put(DayOfWeek.WEDNESDAY, mapTime);
        map.put(DayOfWeek.FRIDAY, mapTime);

        return map;
    }

    public static TreeMap<DayOfWeek, Integer> weekdayAvailability() {
        TreeMap<DayOfWeek, Integer> map = new TreeMap<>();

        map.put(DayOfWeek.WEDNESDAY, 15);
        map.put(DayOfWeek.FRIDAY, 10);

        return map;
    }

    public static List<AppointmentEntity> getAppointmentList() {
        return List.of(AppointmentEntityMock.get());
    }

    public static TreeMap<LocalDate, List<AppointmentEntity>> dayAppointments() {
        TreeMap<LocalDate, List<AppointmentEntity>> map = new TreeMap<>();

        map.put(LocalDate.parse("2024-02-21"), getAppointmentList());
        map.put(LocalDate.parse("2024-02-23"), getAppointmentList());

        return map;
    }

    public static TreeMap<LocalDate, Boolean> monthAvailability() {
        TreeMap<LocalDate, Boolean> map = new TreeMap<>();

        map.put(LocalDate.parse("2024-02-21"), false);
        map.put(LocalDate.parse("2024-02-23"), true);

        return map;
    }

    public static TreeMap<LocalTime, Integer> getTimeAvailability() {
        TreeMap<LocalTime, Integer> map = new TreeMap<>();

        map.put(LocalTime.parse("09:00"), 1);
        map.put(LocalTime.parse("09:30"), 0);
        map.put(LocalTime.parse("10:00"), 1);
        map.put(LocalTime.parse("10:30"), 1);
        map.put(LocalTime.parse("11:00"), 0);
        map.put(LocalTime.parse("11:30"), 1);

        return map;
    }

    public static TreeMap<LocalTime, Boolean> dayAvailability() {
        TreeMap<LocalTime, Boolean> map = new TreeMap<>();

        map.put(LocalTime.parse("09:00"), true);
        map.put(LocalTime.parse("09:30"), false);
        map.put(LocalTime.parse("10:00"), true);
        map.put(LocalTime.parse("10:30"), true);
        map.put(LocalTime.parse("11:00"), false);
        map.put(LocalTime.parse("11:30"), true);

        return map;
    }

    public static TreeMap<LocalTime, List<AppointmentEntity>> hourAppointmentsMap() {
        TreeMap<LocalTime, List<AppointmentEntity>> map = new TreeMap<>();

        map.put(LocalTime.parse("09:30"), getAppointmentList());
        map.put(LocalTime.parse("11:00"), getAppointmentList());

        return map;
    }

    public static TreeMap<LocalTime, List<UUID>> hourScheduleIds() {
        TreeMap<LocalTime, List<UUID>> map = new TreeMap<>();

        map.put(LocalTime.parse("09:00"), List.of(UUID.randomUUID()));
        map.put(LocalTime.parse("09:30"), List.of(UUID.randomUUID()));
        map.put(LocalTime.parse("10:00"), List.of(UUID.randomUUID()));
        map.put(LocalTime.parse("10:30"), List.of(UUID.randomUUID()));
        map.put(LocalTime.parse("11:00"), List.of(UUID.randomUUID()));
        map.put(LocalTime.parse("11:30"), List.of(UUID.randomUUID()));

        return map;
    }
}
