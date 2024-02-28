package br.com.petshop.pet.model.dto.response;

import br.com.petshop.pet.model.enums.Coat;
import br.com.petshop.pet.model.enums.Gender;
import br.com.petshop.pet.model.enums.Size;
import br.com.petshop.pet.model.enums.Specie;
import br.com.petshop.pet.model.enums.Temper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe dto responsável pelo retorno dos dados de um pet.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse implements Serializable {
    private UUID id;
    private UUID customerId;
    private String name;
    private Gender gender;
    private Specie specie;
    private String birthDate;
    private Boolean neutered;
    private Temper temper;
    private Coat coat;
    private Double weight;
    private String breed;
    private Size size;
    private String color;
    private Boolean active;
}
