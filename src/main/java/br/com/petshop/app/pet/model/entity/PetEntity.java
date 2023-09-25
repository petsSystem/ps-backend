package br.com.petshop.app.pet.model.entity;

import br.com.petshop.app.pet.model.enums.PetCoat;
import br.com.petshop.app.pet.model.enums.PetGender;
import br.com.petshop.app.pet.model.enums.PetSize;
import br.com.petshop.app.pet.model.enums.PetSpecie;
import br.com.petshop.app.pet.model.enums.PetTemper;
import br.com.petshop.app.user.model.entity.AppUserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private PetTemper temper;
    @Column(name = "pet_coat")
    private PetCoat coat;
    @Column(name = "pet_weight")
    private String weight;
    @Column(name = "pet_breed")
    private String breed;
    @Column(name = "pet_active")
    private Boolean active;
    @Column(name = "pet_created")
    private LocalDateTime created;
    @Column(name = "pet_updated")
    private LocalDateTime updated;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AppUserEntity appUser;

//
//    @JsonIgnore
//    @ManyToMany(mappedBy = "appUserPets")
//    Set<AppUserEntity> appUsers;
}
