package br.com.petshop.customer.repository;

import br.com.petshop.customer.model.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

    Optional<CustomerEntity> findById(UUID id);
    Optional<CustomerEntity> findByCpf(String cpf);
    Optional<CustomerEntity> findByUsernameAndActiveIsTrue(String username);
    Optional<CustomerEntity> findByEmail(String email);
    Optional<CustomerEntity> findByEmailAndActiveIsTrue(String email);

    Page<CustomerEntity> findAll(Specification<CustomerEntity> filter, Pageable pageable);
}
