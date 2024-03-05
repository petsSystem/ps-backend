package br.com.petshop.appointment.model.entity;

import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.model.enums.PaymentStatus;
import br.com.petshop.appointment.model.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class AppointmentEntityMock {

    public static AppointmentEntity get() {
        return AppointmentEntity.builder()
                .companyId(UUID.randomUUID())
                .petId(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .scheduleId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .userName("Usuário Comum")
                .productId(UUID.randomUUID())
                .additionalIds(List.of(UUID.randomUUID()))
                .date(LocalDate.now())
                .time(LocalTime.now())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentType(null)
                .status(Status.SCHEDULED)
                .comments("Comentário sobre o agendamento.")
                .amount(new BigDecimal(50))
                .active(true)
                .build();
    }
}
