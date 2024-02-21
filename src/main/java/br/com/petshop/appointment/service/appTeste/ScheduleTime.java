package br.com.petshop.appointment.service.appTeste;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTime implements Serializable {
    private Integer availability;
    private List<Integer> usersAvailable;
    private List<Appointment> appointments;
}
