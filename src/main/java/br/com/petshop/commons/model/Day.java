package br.com.petshop.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.DayOfWeek;

/**
 * Objeto de configuração de dias de atendimento na agenda.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Day implements Serializable {
    private DayOfWeek weekday;
    private String initialTime;
    private String endTime;
}
