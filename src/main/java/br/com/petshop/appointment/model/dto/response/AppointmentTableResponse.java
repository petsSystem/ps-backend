package br.com.petshop.appointment.model.dto.response;

import br.com.petshop.appointment.model.enums.Status;
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
public class AppointmentTableResponse implements Serializable {
    private String userName;
    private UUID productId;
    private LocalDate date;
    private LocalTime time;
    private Status status;
}
