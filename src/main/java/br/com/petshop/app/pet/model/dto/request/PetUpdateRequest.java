package br.com.petshop.app.pet.model.dto.request;

import br.com.petshop.app.pet.model.enums.PetCoat;
import br.com.petshop.app.pet.model.enums.PetGender;
import br.com.petshop.app.pet.model.enums.PetSize;
import br.com.petshop.app.pet.model.enums.PetSpecie;
import br.com.petshop.app.pet.model.enums.PetTemper;
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
    private PetGender gender; //sexo
    private PetSpecie species; //especie
    private String dateBirth; //data de nascimento
    private PetSize size; //porte
    private Boolean neutered; //castrado
    private PetTemper temper; //temperamento
    private PetCoat coat; //pelagem
    private String  weight; //peso
    private String  breed; //raça
    private LocalDateTime updated = LocalDateTime.now();

}
