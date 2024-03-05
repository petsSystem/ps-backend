package br.com.petshop.appointment.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Classe dto responsável pela atualização de um agendamento.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentUpdateRequest implements Serializable {
    private UUID petId;
    private UUID scheduleId;
    private UUID userId;
    private String userName;
    private UUID categoryId;
    private UUID productId;
    private List<UUID> additionalIds;
    private String date;
    private String time;
}