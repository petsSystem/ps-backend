package br.com.petshop.system.user.repository;

import br.com.petshop.system.user.model.entity.SysUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SysUserRepository extends JpaRepository<SysUserEntity, Integer> {

    Optional<SysUserEntity> findById(UUID employeeId);
    Optional<SysUserEntity> findByUsernameAndActiveIsTrue(String email);
    Optional<SysUserEntity> findByIdAndActiveIsTrue(UUID employeeId);
    Optional<SysUserEntity> findByCpf(String cpf);
    Page<SysUserEntity> findAll(Specification<SysUserEntity> filter, Pageable pageable);
}
