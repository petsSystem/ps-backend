package br.com.petshop.system.schedule.repository;

import br.com.petshop.system.schedule.model.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {
    List<ScheduleEntity> findByCompanyId(UUID companyId);
    List<ScheduleEntity> findByCompanyIdAndActiveIsTrue(UUID companyId);
}
