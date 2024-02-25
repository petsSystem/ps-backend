package br.com.petshop.schedule.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe dto responsável pelo retorno dos dados de uma agenda.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTableResponse implements Serializable {
    private UUID id;
    private UUID companyId;
    private UUID userId;
    private String name;
    private Boolean active;
}
