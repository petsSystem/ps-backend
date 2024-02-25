package br.com.petshop.appointment.model.dto.request;

import br.com.petshop.appointment.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe dto responsável pela atualização do status de um agendamento.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatusRequest implements Serializable {
    private UUID appointmentId;
    private Status status;
    private String comments;
}