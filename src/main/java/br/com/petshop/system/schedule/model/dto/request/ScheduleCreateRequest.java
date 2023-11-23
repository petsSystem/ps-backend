package br.com.petshop.system.schedule.model.dto.request;

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
public class ScheduleCreateRequest implements Serializable {
    private String category;
    private List<ScheduleDays> days;
    private String intervalMinutes;
    private String companyId;
}
