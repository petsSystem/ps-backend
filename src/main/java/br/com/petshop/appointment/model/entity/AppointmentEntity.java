package br.com.petshop.appointment.model.entity;

import br.com.petshop.commons.audit.AuditorBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointment")
public class AppointmentEntity extends AuditorBaseEntity implements Serializable {
    @Column(name = "pet_id")
    private UUID petId;
    @Column(name = "customer_id")
    private UUID customerId;
    @Column(name = "schedule_id")
    private UUID scheduleId;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "user_name")
    private String userName;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "product_ids", columnDefinition = "jsonb")
    private List<UUID> productIds;
    private LocalDateTime datetime;
    @Column(name = "payment_status")
    private String paymentStatus;
    private String status;
    private String comment;
    private BigDecimal amount;
    private Boolean active;
}
