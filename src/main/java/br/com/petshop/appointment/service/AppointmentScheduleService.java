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

    public static TreeMap<LocalDate, List<AppointmentEntity>> mapAppointments(List<AppointmentEntity> appointments) {
        TreeMap<LocalDate, List<AppointmentEntity>> mapAppointments = new TreeMap<>();
        for(AppointmentEntity app: appointments) {
            List<AppointmentEntity> appointment = mapAppointments.get(app.getDate());
            if (appointment == null)
                appointment = new ArrayList<>();
            appointment.add(app);
            mapAppointments.put(app.getDate(), appointment);
        }

        return mapAppointments;
    }

    public TreeMap<LocalDate, Boolean> getMonthView( TreeMap<DayOfWeek, Integer> structureAvailability,
                                                     TreeMap<LocalDate, List<AppointmentEntity>> appointmentsMap) {
        TreeMap<LocalDate, Boolean> availableDays = new TreeMap<>();

        LocalDate day = LocalDate.now();
        //montar 30 dias...
        LocalDate finalDay = day.plusDays(30);

        while (day.isBefore(finalDay)) {
            Integer schedAvailable = structureAvailability.get(day.getDayOfWeek());

            if (schedAvailable == null) {
                //petshop nao atende nesse dia da semana
                availableDays.put(day, false);
            } else {
                List<AppointmentEntity> appointmentsTime = appointmentsMap.get(day);
                if (appointmentsTime == null) {
                    //há agendamentos vazios
                    availableDays.put(day, true);
                } else {
                    if ((schedAvailable - appointmentsTime.size()) > 0) {
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
        return availableDays;
    }

    public static TreeMap<LocalTime, List<AppointmentEntity>> mapAppointmentsTime(List<AppointmentEntity> appointments) {
        TreeMap<LocalTime, List<AppointmentEntity>> mapAppointments = new TreeMap<>();
        for(AppointmentEntity app: appointments) {
            List<AppointmentEntity> appointment = mapAppointments.get(app.getTime());
            if (appointment == null)
                appointment = new ArrayList<>();
            appointment.add(app);
            mapAppointments.put(app.getTime(), appointment);
        }

        return mapAppointments;
    }

    public TreeMap<LocalTime, Boolean> getDateTimeView(
            TreeMap<LocalTime, Integer> structureAvailability,
            TreeMap<LocalTime, List<AppointmentEntity>> appointmentsTimeMap) {

        TreeMap<LocalTime, Boolean> available = new TreeMap<>();

        for (LocalTime time : structureAvailability.keySet()) {
            Integer timeAvailable = structureAvailability.get(time);
            List<AppointmentEntity> appointments = appointmentsTimeMap.get(time);

            if (appointments == null) {
                //há agendamentos vazios
                available.put(time, true);
            } else {
                if ((timeAvailable - appointments.size()) > 0) {
                    //há agendamentos vazios
                    available.put(time, true);
                } else {
                    //não há vagas no agendamento
                    available.put(time, false);
                }
            }
        }
        return available;
    }
}
