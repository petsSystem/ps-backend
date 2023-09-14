package br.com.petshop.pet.repository;

import br.com.petshop.pet.model.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Integer> {
    Optional<PetEntity> findByIdAndActiveIsTrue(String petId);
}
