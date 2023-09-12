package br.com.petshop.petshop.repository;

import br.com.petshop.user.web.model.entity.WebUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetshopRepository extends JpaRepository<WebUserEntity, Integer> {
    Optional<WebUserEntity> findByEmail(String email);
}
