package br.com.petshop.schedule.model.dto.request;

import br.com.petshop.commons.model.Day;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Classe dto responsável pela criação de uma agenda.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCreateRequest implements Serializable {
    @NotNull
    private UUID companyId;
    @NotNull
    private UUID categoryId;
    @NotNull
    private UUID userId;
    @NotNull
    private List<UUID> productIds;
    private List<Day> days;
    @Builder.Default
    private Boolean active = true;
}
