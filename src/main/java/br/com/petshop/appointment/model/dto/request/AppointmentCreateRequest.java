package br.com.petshop.appointment.model.dto.request;

import br.com.petshop.appointment.model.enums.PaymentStatus;
import br.com.petshop.appointment.model.enums.Status;
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
 * Classe dto responsável pela criação de agendamento.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateRequest implements Serializable {
    @NotNull
    private UUID companyId;
    @NotNull
    private UUID categoryId;
    @NotNull
    private UUID petId;
    @NotNull
    private UUID customerId;
    @NotNull
    private UUID scheduleId;
    @NotNull
    private UUID userId;
    private String userName;
    @NotNull
    private UUID productId;
    private List<UUID> additionalIds;
    @NotNull
    private String date;
    @NotNull
    private String time;

    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    @Builder.Default
    private Status status = Status.SCHEDULED;
    @Builder.Default
    private Boolean active = true;
}
