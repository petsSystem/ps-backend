package br.com.petshop.appointment.repository;

import br.com.petshop.appointment.model.entity.AppointmentEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {
//    Optional<AppointmentEntity> findById(UUID categoryId);
//    Optional<AppointmentEntity> findByUserIdAndProductId(UUID userId, UUID productId);
//    List<AppointmentEntity> findAllByProductId(UUID productId);
//    List<AppointmentEntity> findAll(Specification<AppointmentEntity> filter);
}
