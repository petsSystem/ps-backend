package br.com.petshop.appointment.model.dto.request;

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
public class AppointmentCreateRequest implements Serializable {
    private UUID userId;
    private UUID productId;
    private List<Day> days;
    @Builder.Default
    private Boolean active = true;
}