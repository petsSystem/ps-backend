package br.com.petshop.system.employee.repository;

import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    Optional<EmployeeEntity> findByIdAndActiveIsTrue(String employeeId);
    Optional<EmployeeEntity> findByCpfAndActiveIsTrue(String cnpj);
}
