package br.com.petshop.pet.model.entity;

import br.com.petshop.audit.AuditorBaseEntity;
import br.com.petshop.pet.model.enums.Coat;
import br.com.petshop.pet.model.enums.Gender;
import br.com.petshop.pet.model.enums.Temper;
import br.com.petshop.pet.model.enums.Size;
import br.com.petshop.pet.model.enums.Specie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

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
    private Gender gender;
    private Specie specie;
    @Column(name = "birth_date")
    private String birthDate;
    private String neutered;
    private Temper temper;
    private Coat coat;
    private String weight;
    private String breed;
    private Size size;
    private Boolean active;
}
