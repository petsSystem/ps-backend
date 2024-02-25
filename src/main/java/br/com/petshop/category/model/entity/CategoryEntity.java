package br.com.petshop.category.model.entity;

import br.com.petshop.category.model.enums.Category;
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

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe entidade que representa uma categoria.
 */
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
    @Enumerated(EnumType.STRING)
    private Category type;
    private String description;
    private Boolean active;
}
