package br.com.petshop.category.model.entity;

import br.com.petshop.commons.audit.AuditorBaseEntity;
import br.com.petshop.category.model.enums.Category;
import br.com.petshop.commons.model.Day;
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
@Table(name = "category")
public class CategoryEntity extends AuditorBaseEntity implements Serializable {
    @Column(name = "company_id")
    private UUID companyId;
    private Category type;
    private String description;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "days", columnDefinition = "jsonb")
    private List<Day> days;
    private Boolean active;
}
