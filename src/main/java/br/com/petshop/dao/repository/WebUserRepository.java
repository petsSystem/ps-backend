package br.com.petshop.dao.repository;

import br.com.petshop.dao.entity.WebUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebUserRepository extends JpaRepository<WebUserEntity, Integer> {
    Optional<WebUserEntity> findByEmail(String email);
}
