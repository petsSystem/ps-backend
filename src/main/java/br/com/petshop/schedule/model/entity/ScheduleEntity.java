package br.com.petshop.schedule.model.entity;

import br.com.petshop.commons.audit.AuditorBaseEntity;
import br.com.petshop.commons.model.Day;
import br.com.petshop.schedule.model.dto.Structure;
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
import java.util.List;
import java.util.UUID;

/**
 * Classe entidade que representa uma agenda.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule")
public class ScheduleEntity extends AuditorBaseEntity implements Serializable {
    @Column(name = "company_id")
    private UUID companyId;
    @Column(name = "category_id")
    private UUID categoryId;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "user_name")
    private String name;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "product_ids", columnDefinition = "jsonb")
    private List<UUID> productIds;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "days", columnDefinition = "jsonb")
    private List<Day> days;
    private Boolean active;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "structure", columnDefinition = "jsonb")
    private List<Structure> structure;
}
