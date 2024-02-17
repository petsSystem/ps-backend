package br.com.petshop.appointment.repository;

import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.model.enums.PaymentStatus;
import br.com.petshop.appointment.model.enums.PaymentType;
import br.com.petshop.appointment.model.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {
    Optional<AppointmentEntity> findById(UUID appointmentId);
    List<AppointmentEntity> findAll(Specification<AppointmentEntity> filter);

    @Query(value = "SELECT * FROM appointment WHERE schedule_id = :scheduleId AND date > CURRENT_DATE", nativeQuery = true)
    List<AppointmentEntity> findBySchedule(UUID scheduleId);
}
