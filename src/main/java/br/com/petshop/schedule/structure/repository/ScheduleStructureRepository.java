package br.com.petshop.schedule.structure.repository;

import br.com.petshop.schedule.structure.model.entity.ScheduleStructureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleStructureRepository extends JpaRepository<ScheduleStructureEntity, Integer> {
    Optional<ScheduleStructureEntity> findByProductId(UUID productId);
}
