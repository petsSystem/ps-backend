package br.com.petshop.system.category.repository;

import br.com.petshop.system.category.model.entity.CategoryEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategorySpecification {

    public static Specification<CategoryEntity> filter (UUID companyId) {
        Specification<CategoryEntity> filters = Specification
                .where(companyIdEqual(companyId));

        return filters;
    }

    public static Specification<CategoryEntity> companyIdEqual(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("companyId"), companyId);
        };
    }
}
