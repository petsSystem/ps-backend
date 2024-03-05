package br.com.petshop.appointment.model.dto.response;

import br.com.petshop.appointment.model.enums.PaymentStatus;
import br.com.petshop.appointment.model.enums.Status;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class AppointmentResponseMock {
    public static AppointmentResponse get() {
        return AppointmentResponse.builder()
                .id(UUID.randomUUID())
                .companyId(UUID.randomUUID())
                .petId(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .scheduleId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .userName("Usuário Comum")
                .productId(UUID.randomUUID())
                .additionalIds(List.of(UUID.randomUUID()))
                .date("24-03-2024")
                .time("11:00")
                .paymentStatus(PaymentStatus.PENDING)
                .status(Status.SCHEDULED)
                .comments("Comentários sobre o agendamento.")
                .amount(new BigDecimal(50))
                .active(true)
                .build();
    }
}
