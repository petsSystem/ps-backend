package br.com.petshop.appointment.service.appTeste;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private Integer id;
    private Integer scheduleId;
    private LocalDate date;
    private LocalTime time;
    private String name;
}
