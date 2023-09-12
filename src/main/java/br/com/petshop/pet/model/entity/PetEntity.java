package br.com.petshop.pet.model.entity;

import br.com.petshop.pet.model.enums.PetGender;
import br.com.petshop.pet.model.enums.PetSize;
import br.com.petshop.pet.model.enums.PetSpecie;
import br.com.petshop.user.app.model.entity.AppUserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pet")
public class PetEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pet_id")
    private String id;
    @Column(name = "pet_name")
    private String name;
    @Column(name = "pet_gender")
    private PetGender gender;
    @Column(name = "pet_species")
    private PetSpecie species;
    @Column(name = "pet_date_birth")
    private String dateBirth;
    @Column(name = "pet_size")
    private PetSize size;
    @Column(name = "pet_neutered")
    private String neutered;
    @Column(name = "pet_temper")
    private String temper;
    @Column(name = "pet_coat")
    private String coat;
    @Column(name = "pet_weight")
    private String weight;
    @Column(name = "pet_breed")
    private String breed;

    @JsonIgnore
    @ManyToMany(mappedBy = "appUserPets")
    Set<AppUserEntity> appUsers;
}
