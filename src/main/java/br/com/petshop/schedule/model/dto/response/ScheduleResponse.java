package br.com.petshop.schedule.model.dto.response;

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
 * Classe dto responsável pelo retorno dos dados de uma agenda.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse implements Serializable {
    private UUID id;
    private UUID companyId;
    private UUID categoryId;
    private UUID userId;
    private String name;
    private List<UUID> productIds;
    private List<Day> days;
    private Boolean active;
}
