package br.com.petshop.system.product.repository;

import br.com.petshop.system.product.model.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductSpecification {

    public static Specification<ProductEntity> filter (UUID companyId) {
        Specification<ProductEntity> filters = Specification
                .where(categoryIdEqual(companyId));

        return filters;
    }

    public static Specification<ProductEntity> categoryIdEqual(UUID categoryId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("categoryId"), categoryId);
        };
    }
}
