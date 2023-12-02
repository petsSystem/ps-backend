package br.com.petshop.system.schedule.model.entity;

import br.com.petshop.system.audit.AuditorBaseEntity;
import br.com.petshop.system.schedule.model.dto.request.ScheduleDays;
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

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_schedule")
public class ScheduleEntity extends AuditorBaseEntity implements Serializable {
    private String category;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<ScheduleDays> days;
    private String intervalMinutes;
    private String companyId;
}