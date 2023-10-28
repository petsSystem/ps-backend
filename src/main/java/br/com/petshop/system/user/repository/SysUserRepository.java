package br.com.petshop.system.user.repository;

import br.com.petshop.system.user.model.entity.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SysUserRepository extends JpaRepository<SysUserEntity, Integer> {
    Optional<SysUserEntity> findByEmailAndActiveIsTrue(String email);
    Optional<SysUserEntity> findByIdAndActiveIsTrue(UUID id);
}
