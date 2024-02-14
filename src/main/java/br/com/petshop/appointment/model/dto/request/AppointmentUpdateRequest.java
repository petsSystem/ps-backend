package br.com.petshop.appointment.model.dto.request;

import br.com.petshop.commons.model.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentUpdateRequest implements Serializable {
    private List<Day> days;
}