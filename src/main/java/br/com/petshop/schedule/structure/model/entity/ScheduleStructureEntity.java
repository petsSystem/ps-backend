package br.com.petshop.schedule.structure.model.entity;

import br.com.petshop.schedule.model.dto.Structure;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule_structure")
public class ScheduleStructureEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;
    @Column(name = "product_id")
    private UUID productId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "structure", columnDefinition = "jsonb")
    private List<Structure> structure;
}
