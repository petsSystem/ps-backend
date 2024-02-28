package br.com.petshop.pet.model.entity;

import br.com.petshop.pet.model.enums.Coat;
import br.com.petshop.pet.model.enums.Gender;
import br.com.petshop.pet.model.enums.Size;
import br.com.petshop.pet.model.enums.Specie;
import br.com.petshop.pet.model.enums.Temper;

import java.util.UUID;

public class PetEntityMock {
    public static PetEntity get() {
        return PetEntity.builder()
                .customerId(UUID.randomUUID())
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
                .active(true)
                .build();
    }
}
