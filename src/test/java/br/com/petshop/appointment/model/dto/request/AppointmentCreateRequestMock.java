package br.com.petshop.appointment.model.dto.request;

import br.com.petshop.appointment.model.enums.PaymentStatus;
import br.com.petshop.appointment.model.enums.Status;

import java.util.List;
import java.util.UUID;

public class AppointmentCreateRequestMock {
    public static AppointmentCreateRequest get() {
        return AppointmentCreateRequest.builder()
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
                .active(true)
                .build();
    }
}
