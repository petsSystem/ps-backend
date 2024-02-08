package br.com.petshop.schedule.repository;

import br.com.petshop.schedule.model.entity.ScheduleEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {
    Optional<ScheduleEntity> findById(UUID categoryId);
    Optional<ScheduleEntity> findByUserIdAndProductId(UUID userId, UUID productId);
    List<ScheduleEntity> findAllByProductId(UUID productId);
    List<ScheduleEntity> findAll(Specification<ScheduleEntity> filter);
}