package br.com.petshop.pet.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetUpdateRequest implements Serializable {
    private String name; //nome
    private String gender; //sexo
    private String species; //especie
    private String dateBirth; //data de nascimento
    private String size; //porte
    private Boolean neutered; //castrado
    private String  temper; //temperamento
    private String  coat; //pelagem
    private String  weight; //peso
    private String  breed; //raça
    private LocalDateTime updated = LocalDateTime.now();

}
