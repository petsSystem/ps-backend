package br.com.petshop.appointment.model.dto.response;

import br.com.petshop.commons.model.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse implements Serializable {
    private UUID id;
    private UUID userId;
    private String name;
    private UUID productId;
    private List<Day> days;
    private Boolean active;
}
