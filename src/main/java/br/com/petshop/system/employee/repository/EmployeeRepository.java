package br.com.petshop.system.employee.repository;

import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    Optional<EmployeeEntity> findById(UUID employeeId);
    Optional<EmployeeEntity> findByIdAndActiveIsTrue(UUID employeeId);
    Optional<EmployeeEntity> findByCpfAndActiveIsTrue(String cpf);
    Page<EmployeeEntity> findAll(Specification<EmployeeEntity> filter, Pageable pageable);
}
