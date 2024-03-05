package br.com.petshop.appointment.model.dto.request;

import br.com.petshop.appointment.model.enums.Status;

import java.util.UUID;

public class AppointmentStatusRequestMock {
    public static AppointmentStatusRequest get() {
        return AppointmentStatusRequest.builder()
                .appointmentId(UUID.randomUUID())
                .status(Status.SCHEDULED)
                .comments("Comentários sobre o agendamento.")
                .build();
    }
}