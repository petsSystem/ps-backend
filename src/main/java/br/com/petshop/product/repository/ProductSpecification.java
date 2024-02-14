package br.com.petshop.product.repository;

import br.com.petshop.product.model.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductSpecification {

    public static Specification<ProductEntity> filter (UUID companyId, Boolean additional) {
        Specification<ProductEntity> filters = Specification
                .where(companyIdEqual(companyId))
                .and(additionalEquals(additional));

        return filters;
    }

    public static Specification<ProductEntity> companyIdEqual(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("companyId"), companyId);
        };
    }

    private static Specification<ProductEntity> additionalEquals(Boolean additional) {
        if (additional == null) return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("additional"), additional);
        };
    }
}
