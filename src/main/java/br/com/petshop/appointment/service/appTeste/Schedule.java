package br.com.petshop.appointment.service.appTeste;

import br.com.petshop.commons.model.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private Integer id;
    private Integer userId;
    private String name;
    private List<DayOW> days;
}
