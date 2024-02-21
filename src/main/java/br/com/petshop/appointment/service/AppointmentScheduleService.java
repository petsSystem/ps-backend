package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.entity.AppointmentEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

@Service
public class AppointmentScheduleService {

    public static TreeMap<LocalDate, List<UUID>> mapAppointmentsDays(List<AppointmentEntity> appointments) {
        TreeMap<LocalDate, List<UUID>> mapAppointments = new TreeMap<>();
        for(AppointmentEntity app: appointments) {
            List<UUID> appointmentIds = mapAppointments.get(app.getDate());
            if (appointmentIds == null)
                appointmentIds = new ArrayList<>();
            appointmentIds.add(app.getId());
            mapAppointments.put(app.getDate(), appointmentIds);
        }

        return mapAppointments;
    }

    public TreeMap<LocalDate, Boolean> getMonthView( TreeMap<DayOfWeek, Integer> structureAvailability,
                                                     List<AppointmentEntity> appointments,
                                                     TreeMap<LocalDate, List<UUID>> appointmentsMap) {
        TreeMap<LocalDate, Boolean> availableDays = new TreeMap<>();

        LocalDate day = LocalDate.now();
        //montar 30 dias...
        LocalDate finalDay = day.plusDays(30);

        while (day.isBefore(finalDay)) {
            //verificar qual o dia da semana do dia
            DayOfWeek week = day.getDayOfWeek();

            Integer schedAvailable = structureAvailability.get(week);

            if (schedAvailable == null) {
                //petshop nao atende nesse dia da semana
                availableDays.put(day, false);
            } else {
                List<UUID> appointmentsTime = appointmentsMap.get(day);
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

    public static TreeMap<LocalTime, List<AppointmentEntity>> mapAppointments(LocalDate date, List<AppointmentEntity> appointments) {
        TreeMap<LocalTime, List<AppointmentEntity>> mapAppointments = new TreeMap<>();
        for(AppointmentEntity app: appointments) {
            if (app.getDate().equals(date)) {
                List<AppointmentEntity> appointmentsTime = mapAppointments.get(app.getTime());
                if (appointmentsTime == null)
                    appointmentsTime = new ArrayList<>();
                appointmentsTime.add(app);
                mapAppointments.put(app.getTime(), appointmentsTime);
            }
        }
        return mapAppointments;
    }
}
