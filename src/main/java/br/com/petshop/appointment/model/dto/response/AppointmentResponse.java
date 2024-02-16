package br.com.petshop.appointment.model.dto.response;

import br.com.petshop.appointment.model.enums.PaymentStatus;
import br.com.petshop.appointment.model.enums.PaymentType;
import br.com.petshop.appointment.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse implements Serializable {
    private UUID companyId;
    private UUID petId;
    private UUID customerId;
    private UUID scheduleId;
    private UUID userId;
    private String userName;
    private UUID productId;
    private List<UUID> additionalIds;
    private LocalDate date;
    private LocalTime time;
    private PaymentStatus paymentStatus;
    private PaymentType paymentType;
    private Status status;
    private String comments;
    private BigDecimal amount;
    private Boolean active;
}
