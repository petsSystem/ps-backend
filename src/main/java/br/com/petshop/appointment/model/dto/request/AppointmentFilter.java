package br.com.petshop.appointment.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentFilter implements Serializable {
    private UUID customerId;
    private UUID scheduleId;
    private UUID userId;
    private UUID productId;
    private LocalDateTime datetime;
    private String paymentStatus;
    private String status;
    private BigDecimal amount;
    private Boolean active;
}
