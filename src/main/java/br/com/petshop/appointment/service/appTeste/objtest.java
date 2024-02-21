package br.com.petshop.appointment.service.appTeste;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class objtest implements Serializable {
    LocalTime nome;
    List<UUID> ids;

    @Override
    public String toString() {
        String idsList = "";
        for(UUID id : ids) {
            if (idsList != ""){
                idsList += ",";
            }
            idsList += "\"" + id + "\"";
        }

        return "{\n" +
                "    \"nome\": \"" + nome + "\",\n" +
                "    \"ids\": [" + idsList + "]\n" +
                "}";

    }
}
