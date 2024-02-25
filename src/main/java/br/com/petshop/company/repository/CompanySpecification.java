package br.com.petshop.company.repository;

import br.com.petshop.user.model.entity.UserEntity;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Classe de queries filtro da loja/petshop
 */
@Service
public class CompanySpecification {

    public static Specification<UserEntity> filter (UUID companyId, UUID productId) {
        Specification<UserEntity> filters = Specification
                .where(companyIdInList(companyId));

        if (productId != null)
            filters.and(productIdInList(productId));

        return filters;
    }

    private static Specification<UserEntity> companyIdInList(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            Expression toJsonbArray = criteriaBuilder.function("jsonb_build_array", UUID.class, criteriaBuilder.literal(companyId));
            return criteriaBuilder.equal(criteriaBuilder.function("jsonb_contains", String.class, root.get("companyIds"), toJsonbArray), true);
        };
    }

    private static Specification<UserEntity> productIdInList(UUID productId) {
        return (root, query, criteriaBuilder) -> {
            Expression toJsonbArray = criteriaBuilder.function("jsonb_build_array", UUID.class, criteriaBuilder.literal(productId));
            return criteriaBuilder.equal(criteriaBuilder.function("jsonb_contains", String.class, root.get("productIds"), toJsonbArray), true);
        };
    }
}
