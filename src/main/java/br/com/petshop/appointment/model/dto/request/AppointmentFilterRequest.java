package br.com.petshop.appointment.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Classe dto responsável pelo filtro de agendamento.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentFilterRequest implements Serializable {
    private UUID companyId;
    private UUID categoryId;
    private UUID productId;
    private UUID userId;
    private LocalDate date;
}
