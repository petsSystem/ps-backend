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

/**
 * Classe dto responsável pela atualização de uma agenda.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleUpdateRequest implements Serializable {
    private UUID companyId;
    private UUID categoryId;
    private List<UUID> productIds;
    private List<Day> days;
}