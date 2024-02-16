package br.com.petshop.appointment.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentFilterRequest implements Serializable {
    private UUID companyId;
    private UUID customerId;
    private UUID scheduleId;
    private UUID userId;
    private UUID productId;
    private LocalDate date;
    private LocalTime time;
    private String paymentStatus;
    private String status;
}
