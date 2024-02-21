package br.com.petshop.appointment.service.appTeste;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class dayTest {

    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    static Map<DayOfWeek, Integer> available = new HashMap<>();


    public static void main(String[] args) {

//        //mock de agendas de usuarios
//        List<Schedule> schedules = mockUserSchedules();
//
//        //CRIO A AGENDA MATRIZ DE DISPONIBILIDADE (AGENDA DE TODOS OS SCHEDULES MERGEADOS)
//        TreeMap<DayOfWeek, TreeMap<LocalTime, List<Integer>>> mapSchedule = createScheduleStructure(schedules);
//
//        //VERIFICAR A QUANTIDADE DE AGENDAMENTOS POSSIVEIS POR DIA DA SEMANA
//        mapSchedule.forEach((k, v) -> availability(k, v));
//        System.out.println("AVAILABLE: " + available);
//
//        //CRIAR OS AGENDAMENTOS
//        List<Appointment> appointments = mockAppointments();
//
//        //RECUPERAR TODOS OS APPOINTMENTS e separa-los por dia
//        TreeMap<LocalDate, List<Integer>> mapAppointments = mapAppointmentsDays(appointments);
//
//        //MERGE DE DISPONIBILIDADE DE AGENDA (visualização mensal) - fixo 3 meses
//        TreeMap<LocalDate, Boolean> availableMensalDays = getMensalView(appointments, mapAppointments);
//
//        LocalDate today = LocalDate.now();
//        //MERGE DE DISPONIBILIDADE DE AGENDA NO DIA

        TreeMap<LocalTime, List<Appointment>> availableDay = getDayView(LocalDate.now());
        System.out.println(availableDay);

    }

    private static TreeMap<LocalTime, List<Appointment>> getDayView(LocalDate date) {

        //mock de agendas de usuarios
        List<Schedule> schedules = mockUserSchedules();

        //CRIO A AGENDA MATRIZ DE DISPONIBILIDADE (AGENDA DE TODOS OS SCHEDULES MERGEADOS)
        TreeMap<DayOfWeek, TreeMap<LocalTime, List<Integer>>> mapSchedule = createScheduleStructure(schedules);

        //CRIAR OS AGENDAMENTOS
        List<Appointment> appointments = mockAppointments();

        //RECUPERAR TODOS OS APPOINTMENTS do dia e separa-los por horario
        return mapAppointments(date, appointments);

    }

    private static TreeMap<LocalTime, List<Appointment>> mapAppointments(LocalDate date, List<Appointment> appointments) {
        TreeMap<LocalTime, List<Appointment>> mapAppointments = new TreeMap<>();
        for(Appointment app: appointments) {
            if (app.getDate().equals(date)) {
                List<Appointment> appointmentsTime = mapAppointments.get(app.getTime());
                if (appointmentsTime == null)
                    appointmentsTime = new ArrayList<>();
                appointmentsTime.add(app);
                mapAppointments.put(app.getTime(), appointments);
            }
        }

        return mapAppointments;
    }

    private static TreeMap<LocalDate, Boolean> getMensalView(List<Appointment> appointments, TreeMap<LocalDate, List<Integer>> mapAppointments) {

        TreeMap<LocalDate, Boolean> availableDays = new TreeMap<>();

        LocalDate day = LocalDate.now();
        //montar 30 dias...
        LocalDate finalDay = day.plusMonths(3);

        while (day.isBefore(finalDay)) {
            //verificar qual o dia da semana do dia
            DayOfWeek week = day.getDayOfWeek();

            Integer schedAvailable = available.get(week);

            if (schedAvailable == null) {
                //petshop nao atende nesse dia da semana
                availableDays.put(day, false);
            } else {
                List<Integer> appointmentsTime = mapAppointments.get(day);
                if (appointmentsTime == null) {
                    //há agendamentos vazios
                    availableDays.put(day, true);
                } else {
                    if (schedAvailable - appointments.size() > 0) {
                        //há agendamentos vazios
                        availableDays.put(day, true);
                    } else {
                        //não há vagas no agendamento
                        availableDays.put(day, false);
                    }
                }
            }
            day = day.plusDays(1);
        }

        System.out.println(availableDays);

        return availableDays;
    }

    private static TreeMap<DayOfWeek, TreeMap<LocalTime, List<Integer>>> createScheduleStructure(List<Schedule> schedules) {
        Integer intervalMinutes = 30;
//        Map<LocalTime, List<Integer>> map1 = null;
        TreeMap<DayOfWeek, TreeMap<LocalTime, List<Integer>>> mapSchedule = new TreeMap<>();
        TreeMap<LocalTime, List<Integer>> map1 = null;

        for (Schedule sch : schedules) {
            System.out.println(sch.getId() + " - " + sch.getName());
            for (DayOW dow : sch.getDays()) {
                map1 = mapSchedule.get(dow.getWeekday());
                TreeMap<LocalTime, List<Integer>> scheduleTime = dow.getScheduleTime(sch.getId(), intervalMinutes);

                if (map1 == null)

                    mapSchedule.put(dow.getWeekday(), scheduleTime);
                else {
                    Map<LocalTime, List<Integer>> map2 = scheduleTime;
                    map1 = Stream.concat(map1.entrySet().stream(), map2.entrySet().stream())
                            .collect(Collectors.groupingBy(e-> e.getKey(),
                                    TreeMap::new,
                                    Collectors.flatMapping(e -> e.getValue().stream(), Collectors.toList())));

                    mapSchedule.put(dow.getWeekday(), map1);
                }
            }
        }
        System.out.println("MAP_SCHEDULE: " + mapSchedule);
        return mapSchedule;
    }

    public static void availability(DayOfWeek k, Map<LocalTime, List<Integer>> v) {
        Integer count = available.get(k);
        if (count == null)
            count = 0;
        count = count + v.size();
        available.put(k, count);
    }

    private static TreeMap<LocalDate, List<Integer>> mapAppointmentsDays(List<Appointment> appointments) {
        TreeMap<LocalDate, List<Integer>> mapAppointments = new TreeMap<>();
        for(Appointment a: appointments) {
            List<Integer> appointmentIds = mapAppointments.get(a.getDate());
            if (appointmentIds == null)
                appointmentIds = new ArrayList<>();
            appointmentIds.add(a.getId());
            mapAppointments.put(a.getDate(), appointmentIds);
        }

        System.out.println("MAP_APPOINTMENTS: " + mapAppointments);
        return mapAppointments;
    }

    public static List<Schedule> mockUserSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        List<DayOW> days = new ArrayList<>();
        DayOW d = DayOW.builder()
                .weekday(DayOfWeek.WEDNESDAY)
                .initialTime("07:00")
                .endTime("13:00")
                .build();
        days.add(d);

        d = DayOW.builder()
                .weekday(DayOfWeek.FRIDAY)
                .initialTime("07:00")
                .endTime("17:00")
                .build();
        days.add(d);

        Schedule schedule = Schedule.builder()
                .id(1)
                .userId(1)
                .name("Van")
                .days(days)
                .build();

        schedules.add(schedule);

        days = new ArrayList<>();
        d = DayOW.builder()
                .weekday(DayOfWeek.MONDAY)
                .initialTime("09:30")
                .endTime("17:00")
                .build();
        days.add(d);

        d = DayOW.builder()
                .weekday(DayOfWeek.WEDNESDAY)
                .initialTime("08:30")
                .endTime("19:00")
                .build();
        days.add(d);

        d = DayOW.builder()
                .weekday(DayOfWeek.FRIDAY)
                .initialTime("12:30")
                .endTime("20:00")
                .build();
        days.add(d);

        schedule = Schedule.builder()
                .id(2)
                .userId(2)
                .name("Was")
                .days(days)
                .build();

        schedules.add(schedule);
        
        return schedules;
    }

    private static List<Appointment> mockAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        Appointment app = Appointment.builder()
                .id(1)
                .scheduleId(1)
                .date(LocalDate.parse("19-02-2024", dateFormatter))
                .time(LocalTime.parse("09:00", timeFormatter))
                .name("Sophie")
                .build();

        appointments.add(app);

        app = Appointment.builder()
                .id(2)
                .scheduleId(1)
                .date(LocalDate.parse("19-02-2024", dateFormatter))
                .time(LocalTime.parse("09:30", timeFormatter))
                .name("Lady")
                .build();

        appointments.add(app);

        app = Appointment.builder()
                .id(3)
                .scheduleId(1)
                .date(LocalDate.parse("21-02-2024", dateFormatter))
                .time(LocalTime.parse("07:00", timeFormatter))
                .name("Sara")
                .build();

        appointments.add(app);

        app = Appointment.builder()
                .id(4)
                .scheduleId(2)
                .date(LocalDate.parse("21-02-2024", dateFormatter))
                .time(LocalTime.parse("09:00", timeFormatter))
                .name("Sophie")
                .build();

        appointments.add(app);

        return appointments;
    }
}
