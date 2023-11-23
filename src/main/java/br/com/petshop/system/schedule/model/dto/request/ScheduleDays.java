package br.com.petshop.system.schedule.model.dto.request;

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
public class ScheduleDays implements Serializable {
    private String dayOfWeek;
    private String initialTime;
    private String endTime;
    private Integer simultaneos;
    private String intervalMinutes;
}
