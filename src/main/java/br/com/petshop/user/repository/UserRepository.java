package br.com.petshop.user.repository;

import br.com.petshop.user.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findById(UUID employeeId);
    Optional<UserEntity> findByUsernameAndActiveIsTrue(String cpf);
    Optional<UserEntity> findByIdAndActiveIsTrue(UUID employeeId);
    Optional<UserEntity> findByCpf(String cpf);
    Page<UserEntity> findAll(Specification<UserEntity> filter, Pageable pageable);
}
