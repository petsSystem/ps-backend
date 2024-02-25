package br.com.petshop.customer.repository;

import br.com.petshop.customer.model.entity.CustomerEntity;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Classe de queries filtro de cliente
 */
@Service
public class CustomerSpecification {

    public static Specification<CustomerEntity> filter (UUID companyId) {
        Specification<CustomerEntity> filters = Specification
                .where(companyIdInList(companyId));

        return filters;
    }

    public static Specification<CustomerEntity> companyIdInList(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            Expression toJsonbArray = criteriaBuilder.function("jsonb_build_array", UUID.class, criteriaBuilder.literal(companyId));
            return criteriaBuilder.equal(criteriaBuilder.function("jsonb_contains", String.class, root.get("companyIds"), toJsonbArray), true);
        };
    }
}
