package br.com.petshop.system.schedule.model.dto.response;

import br.com.petshop.system.schedule.model.dto.request.ScheduleDays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse implements Serializable {
    private UUID id;
    private String category;
    private List<ScheduleDays> days;
    private String intervalMinutes;
    private String companyId;
    private String active;
    private LocalDateTime createdAt;
}
