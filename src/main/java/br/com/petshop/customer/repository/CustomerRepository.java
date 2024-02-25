package br.com.petshop.customer.repository;

import br.com.petshop.customer.model.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Classe repositório do cliente
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

    Optional<CustomerEntity> findById(UUID id);
    Optional<CustomerEntity> findByCpfAndActiveIsTrue(String cpf);
    Optional<CustomerEntity> findByUsernameAndActiveIsTrue(String username);
    Page<CustomerEntity> findAll(Specification<CustomerEntity> filter, Pageable pageable);
}
