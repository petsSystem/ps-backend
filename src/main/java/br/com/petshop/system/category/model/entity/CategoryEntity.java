package br.com.petshop.system.category.model.entity;

import br.com.petshop.system.audit.AuditorBaseEntity;
import br.com.petshop.system.category.model.enums.Category;
import br.com.petshop.system.model.Day;
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

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_category")
public class CategoryEntity extends AuditorBaseEntity implements Serializable {
    private UUID companyId;
    private Category type;
    private String label;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "days", columnDefinition = "jsonb")
    private List<Day> days;
    private Boolean active;
}
