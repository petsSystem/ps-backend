package br.com.petshop.schedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Classe dto responsável pelo retorno dos horários de agendamento, que farão parte da estrutura da agenda.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Times implements Serializable {
    private LocalTime time;
    private List<UUID> scheduleIds;

    @Override
    public String toString() {
        String idsList = "";
        for(UUID id : scheduleIds) {
            if (idsList != ""){
                idsList += ",";
            }
            idsList += "\"" + id + "\"";
        }

        return "{\n" +
                "\"time\": \"" + time + "\",\n" +
                "\"scheduleIds\": [" + idsList + "]\n" +
                "}";
    }
}
