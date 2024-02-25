package br.com.petshop.appointment.model.dto.request;

import java.util.List;
import java.util.UUID;

public class AppointmentUpdateRequestMock {
    public static AppointmentUpdateRequest get() {
        return AppointmentUpdateRequest.builder()
                .petId(UUID.randomUUID())
                .scheduleId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .userName("Usuário Comum")
                .productId(UUID.randomUUID())
                .additionalIds(List.of(UUID.randomUUID()))
                .date("24-03-2024")
                .time("11:00")
                .build();
    }
}