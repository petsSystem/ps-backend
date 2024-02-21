package br.com.petshop.appointment.service.appTeste;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTest implements Serializable {
    private LocalTime time;
    private List<Integer> scheduleIds;
}
