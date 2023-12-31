package br.com.petshop.app.pet.repository;

import br.com.petshop.app.pet.model.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Integer> {
    Optional<PetEntity> findByIdAndActiveIsTrue(String petId);
    List<PetEntity> findByAppUser_EmailAndActiveIsTrue(String email);
}
