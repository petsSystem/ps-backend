package br.com.petshop.schedule.structure.service;

import br.com.petshop.schedule.model.dto.Structure;
import br.com.petshop.schedule.model.dto.Times;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Classe responsável por transformar objetos para estruturas das agendas x agendamentos
 */
@Service
public class ScheduleStructureConverterService {

    /**
     * Método que transforma uma árvore dia da semana x (árvore horário x lista de ids de agendamentos)
     * em uma lista de objetos do tipo estrutura
     * @param mapSchedule - árvore dia da semana x (árvore horário x lista de ids de agendamentos)
     * @return - lista de estrutura
     */
    public List<Structure> mapToList(TreeMap<DayOfWeek, TreeMap<LocalTime, List<UUID>>> mapSchedule) {
        List<Structure> structures = new ArrayList<>();
        for (DayOfWeek dow : mapSchedule.keySet()) {
            TreeMap<LocalTime, List<UUID>> mapTimes = mapSchedule.get(dow);
            List<String> times = mapToListTime(mapTimes);

            Structure structure = Structure.builder()
                    .weekday(dow)
                    .times(times)
                    .build();
            structures.add(structure);
        }
        return structures;
    }

    /**
     * Método que transforma uma árvore de horários e lista de ids de agendas em uma lista de texto.
     * @param mapTimes - árvore de horários x lista de ids de agendas
     * @return - lista de texto
     */
    private List<String> mapToListTime(TreeMap<LocalTime, List<UUID>> mapTimes) {
        List<String> times = new ArrayList<>();
        for (LocalTime hour : mapTimes.keySet()) {
            List<UUID> scheduleIds = mapTimes.get(hour);

            Times time = Times.builder()
                    .time(hour)
                    .scheduleIds(scheduleIds)
                    .build();
            times.add(time.toString());
        }
        return times;
    }
}
