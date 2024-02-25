package br.com.petshop.appointment.model.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public class AppointmentFilterRequestMock {
    public static AppointmentFilterRequest get() {
        return AppointmentFilterRequest.builder()
                .companyId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .date(LocalDate.now())
                .build();
    }
}
