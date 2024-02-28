package br.com.petshop.pet.model.dto.request;

import br.com.petshop.pet.model.enums.Coat;
import br.com.petshop.pet.model.enums.Gender;
import br.com.petshop.pet.model.enums.Size;
import br.com.petshop.pet.model.enums.Specie;
import br.com.petshop.pet.model.enums.Temper;

public class PetUpdateRequestMock {
    public static PetUpdateRequest get() {
        return PetUpdateRequest.builder()
                .name("Pet name")
                .gender(Gender.MALE)
                .specie(Specie.DOG)
                .birthDate("01/01/2001")
                .neutered(true)
                .temper(Temper.EASY)
                .coat(Coat.SHORT)
                .weight(4.5)
                .breed("Yorkshire Terrier")
                .size(Size.P)
                .color("Azul aço e caramelo")
                .build();
    }
}
