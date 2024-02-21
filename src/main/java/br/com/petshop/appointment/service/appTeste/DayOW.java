package br.com.petshop.appointment.service.appTeste;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayOW implements Serializable {
    private DayOfWeek weekday;
    private String initialTime;
    private String endTime;
    private Map<LocalTime, List<Integer>> scheduleTime;

    public TreeMap<LocalTime, List<Integer>> getScheduleTime(Integer scheduleId, Integer intervalMinutes) {
        TreeMap<LocalTime, List<Integer>> scheduleTime = new TreeMap<>();

        LocalTime initial = LocalTime.parse(initialTime);
        LocalTime end = LocalTime.parse(endTime);
        while(initial.isBefore(end)) {
            scheduleTime.put(initial, List.of(scheduleId));
            initial = initial.plusMinutes(intervalMinutes);
        }
        return scheduleTime;
    }
}
