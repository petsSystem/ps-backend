package br.com.petshop.pet.model.dto.request;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetCreateRequest implements Serializable {
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
    @Builder.Default
    private Boolean active = true;
}
