package br.com.petshop.system.schedule.repository;

import br.com.petshop.system.schedule.model.entity.ScheduleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {

    Optional<ScheduleEntity> findById(UUID employeeId);
//    Optional<ScheduleEntity> findByIdAndActiveIsTrue(UUID employeeId);
//    Optional<ScheduleEntity> findByCpfAndActiveIsTrue(String cpf);
//    Page<ScheduleEntity> findAll(Specification<ScheduleEntity> filter, Pageable pageable);
}
