package br.com.petshop.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Day implements Serializable {
    private Weekday weekday;
    private String initialTime;
    private String endTime;
    private Integer simultaneous;
    private Integer intervalMinutes;
}
