package br.com.petshop.customer.repository;

import br.com.petshop.customer.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    Optional<CustomerEntity> findByCpf(String cpf);
    Optional<CustomerEntity> findByUsernameAndActiveIsTrue(String username);
    Optional<CustomerEntity> findByEmail(String email);
    Optional<CustomerEntity> findByEmailAndActiveIsTrue(String email);
}
