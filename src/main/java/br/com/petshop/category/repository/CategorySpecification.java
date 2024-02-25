package br.com.petshop.category.repository;

import br.com.petshop.category.model.entity.CategoryEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Classe de queries filtro da categoria
 */
@Service
public class CategorySpecification {

    public static Specification<CategoryEntity> filter (UUID companyId, Boolean active) {
        Specification<CategoryEntity> filters = Specification
                .where(companyIdEqual(companyId))
                .and(activeEqual(active));

        return filters;
    }

    public static Specification<CategoryEntity> companyIdEqual(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("companyId"), companyId);
        };
    }

    public static Specification<CategoryEntity> activeEqual(Boolean active) {
        if (active == null) return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }
}
