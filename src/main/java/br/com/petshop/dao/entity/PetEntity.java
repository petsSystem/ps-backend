package br.com.petshop.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pet")
public class PetEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pet_name")
    private String name;
    @Column(name = "pet_gender")
    private String gender;
    @Column(name = "pet_species")
    private String species;
    @Column(name = "pet_date_birth")
    private String dateBirth;
    @Column(name = "pet_size")
    private String size;
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

    @ManyToMany(mappedBy = "appUserPets")
    Set<AppUserEntity> appUsers;
}
