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

/**
 * Classe responsável pelas regras da construção de agendamento
 */
@Service
public class AppointmentScheduleService {

    /**
     * Transforma uma lista de entidade de agendamento em um map de agendamento, sendo a data a key.
     * @param appointments - lista de agendamentos
     * @return - árvore de data x lista de agendamentos
     */
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

    /**
     * Método que retorna o dia e a disponibilidade (se há = true, se não há = false)
     * @param structureAvailability - estrutura de disponibilidade de dia da semana x quantidade de horários vagos
     * @param appointmentsMap - mapa de dia x lista de agendamentos
     * @return = árvore de dia x disponibiidade
     */
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

    /**
     * Transforma uma lista de entidade de agendamento em um map de agendamento, sendo o time a key.
     * @param appointments - Lista de agendamentos
     * @return - árvore de horário x lista de agendamentos
     */
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

    /**
     * Método que retorna o horário e a disponibilidade (se há = true, se não há = false)
     * @param structureAvailability - estrutura de disponibilidade de horário x quantidade de vagas disponíveis
     * @param appointmentsTimeMap - mapa de horário x lista de agendamentos
     * @return - árvore de horário x disponibilidade
     */
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

    /**
     * Método que retorna o horário e os agendamentos, dado um determinado dia
     * @param structureTime - estrutura de horário x lista de ids de agendamentos
     * @param appointmentsTimeMap - mapa de horário x lista de agendamentos
     * @return - árvore de horário x lista de agendamentos
     */
    public TreeMap<LocalTime, List<AppointmentEntity>> getScheduleDateTimeView(
            TreeMap<LocalTime, List<UUID>> structureTime,
            TreeMap<LocalTime, List<AppointmentEntity>> appointmentsTimeMap) {

        TreeMap<LocalTime,  List<AppointmentEntity>> times = new TreeMap<>();

        for (LocalTime time : structureTime.keySet()) {
            List<AppointmentEntity> timeAvailable = appointmentsTimeMap.get(time);

            times.put(time, timeAvailable);
        }
        return times;
    }
}
