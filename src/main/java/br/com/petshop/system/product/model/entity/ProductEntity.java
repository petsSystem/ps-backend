package br.com.petshop.system.product.model.entity;

import br.com.petshop.system.audit.AuditorBaseEntity;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_product")
public class ProductEntity extends AuditorBaseEntity implements Serializable {
    @Column(name = "company_id")
    private UUID companyId;
    @Column(name = "category_id")
    private UUID categoryId;
    private String name;
    private BigDecimal amount;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "days", columnDefinition = "jsonb")
    private List<Day> days;
    private Boolean additional;
    private Integer appointmentConfig;
    private Boolean active;
}
