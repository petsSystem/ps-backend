package br.com.petshop.pet.repository;

import br.com.petshop.pet.model.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Classe repositório do pet
 */
@Repository
public interface PetRepository extends JpaRepository<PetEntity, Integer> {
    Optional<PetEntity> findByIdAndActiveIsTrue(UUID petId);
    List<PetEntity> findByCustomerIdAndActiveIsTrue(UUID customerId);
}
