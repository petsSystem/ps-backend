package br.com.petshop.schedule.repository;

import br.com.petshop.schedule.model.entity.ScheduleEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Classe repositório da agenda
 */
@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {
    Optional<ScheduleEntity> findById(UUID scheduleId);
    Optional<ScheduleEntity> findByCompanyIdAndCategoryIdAndUserId(UUID companyId, UUID categoryId, UUID userId);
    List<ScheduleEntity> findAll(Specification<ScheduleEntity> filter);
    Optional<ScheduleEntity> findByUserId(UUID userId);

    @Query(value = "SELECT * FROM schedule WHERE product_ids in (:productId)", nativeQuery = true)
    List<ScheduleEntity> findByProductId(UUID productId);
}
