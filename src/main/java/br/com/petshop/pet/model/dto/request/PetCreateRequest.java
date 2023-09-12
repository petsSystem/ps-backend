package br.com.petshop.pet.model.dto.request;

import br.com.petshop.pet.model.enums.PetGender;
import br.com.petshop.pet.model.enums.PetSize;
import br.com.petshop.pet.model.enums.PetSpecie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetCreateRequest implements Serializable {
    private String name; //nome
    private PetGender gender; //sexo
    private PetSpecie species; //especie
    private PetSize size; //porte
}
