package br.com.petshop.system.employee.repository;

import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    Optional<EmployeeEntity> findById(UUID employeeId);
    Optional<EmployeeEntity> findByIdAndActiveIsTrue(UUID employeeId);
    Optional<EmployeeEntity> findByCpfAndActiveIsTrue(String cpf);
}
