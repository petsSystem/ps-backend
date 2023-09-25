package br.com.petshop.system.user.repository;

import br.com.petshop.system.user.model.entity.SystemUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUserEntity, Integer> {
    Optional<SystemUserEntity> findByEmailAndActiveIsTrue(String email);
}
