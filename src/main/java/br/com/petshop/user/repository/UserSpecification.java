package br.com.petshop.user.repository;

import br.com.petshop.user.model.entity.UserEntity;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Classe de queries filtro do usuário do sistema.
 */
@Service
public class UserSpecification {

    public static Specification<UserEntity> filter (UUID companyId) {
        Specification<UserEntity> filters = Specification
                .where(companyIdInList(companyId));

        return filters;
    }

    private static Specification<UserEntity> companyIdInList(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            Expression toJsonbArray = criteriaBuilder.function("jsonb_build_array", UUID.class, criteriaBuilder.literal(companyId));
            return criteriaBuilder.equal(criteriaBuilder.function("jsonb_contains", String.class, root.get("companyIds"), toJsonbArray), true);
        };
    }
}
