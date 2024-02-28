package br.com.petshop.pet.model.entity;

import br.com.petshop.commons.audit.AuditorBaseEntity;
import br.com.petshop.pet.model.enums.Coat;
import br.com.petshop.pet.model.enums.Gender;
import br.com.petshop.pet.model.enums.Temper;
import br.com.petshop.pet.model.enums.Size;
import br.com.petshop.pet.model.enums.Specie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe entidade que representa um pet.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pet")
public class PetEntity extends AuditorBaseEntity implements Serializable {
    private UUID customerId;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Specie specie;
    @Column(name = "birth_date")
    private String birthDate;
    private Boolean neutered;
    @Enumerated(EnumType.STRING)
    private Temper temper;
    @Enumerated(EnumType.STRING)
    private Coat coat;
    private Double weight;
    private String breed;
    @Enumerated(EnumType.STRING)
    private Size size;
    private String color;
    private Boolean active;
}
