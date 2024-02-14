package br.com.petshop.schedule.model.dto.request;

import br.com.petshop.commons.model.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCreateRequest implements Serializable {
    private UUID userId;
    private List<UUID> productIds;
    private List<Day> days;
    @Builder.Default
    private Boolean active = true;
}
