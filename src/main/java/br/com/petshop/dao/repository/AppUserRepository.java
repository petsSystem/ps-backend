package br.com.petshop.dao.repository;

import br.com.petshop.dao.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Integer> {
    Optional<AppUserEntity> findByEmail(String email);
}
