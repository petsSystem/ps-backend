package br.com.petshop.appointment.model.dto.request;

import br.com.petshop.appointment.model.enums.PaymentStatus;
import br.com.petshop.appointment.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateRequest implements Serializable {
    private UUID companyId;
    private UUID petId;
    private UUID customerId;
    private UUID scheduleId;
    private UUID userId;
    private UUID productId;
    private List<UUID> additionalIds;
    private LocalDate date;
    private LocalTime time;

    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    @Builder.Default
    private Status status = Status.CREATED;
    @Builder.Default
    private Boolean active = true;
}
