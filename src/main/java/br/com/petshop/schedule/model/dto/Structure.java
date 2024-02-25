package br.com.petshop.schedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.List;

/**
 * Classe dto responsável pela estrutura de agendas mergeadas ou individuais.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Structure implements Serializable {
    private DayOfWeek weekday;
    private List<String> times;
}
