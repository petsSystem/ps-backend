package br.com.petshop.appointment.model.entity;

import br.com.petshop.appointment.model.enums.PaymentStatus;
import br.com.petshop.appointment.model.enums.PaymentType;
import br.com.petshop.appointment.model.enums.Status;
import br.com.petshop.commons.audit.AuditorBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Classe entidade que representa um agendamento.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointment")
public class AppointmentEntity extends AuditorBaseEntity implements Serializable {
    @Column(name = "company_id")
    private UUID companyId;
    @Column(name = "pet_id")
    private UUID petId;
    @Column(name = "customer_id")
    private UUID customerId;
    @Column(name = "category_id")
    private UUID categoryId;
    @Column(name = "schedule_id")
    private UUID scheduleId;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "product_id")
    private UUID productId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "product_ids", columnDefinition = "jsonb")
    private List<UUID> additionalIds;
    private LocalDate date;
    private LocalTime time;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String comments;
    private BigDecimal amount;
    private Boolean active;
}
