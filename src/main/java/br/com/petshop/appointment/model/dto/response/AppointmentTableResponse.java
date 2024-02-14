package br.com.petshop.appointment.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTableResponse implements Serializable {
    private UUID id;
    private UUID userId;
    private String name;
    private Boolean active;
}
