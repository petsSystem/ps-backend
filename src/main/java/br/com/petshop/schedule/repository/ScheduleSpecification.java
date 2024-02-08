package br.com.petshop.schedule.repository;

import br.com.petshop.schedule.model.entity.ScheduleEntity;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScheduleSpecification {

    public static Specification<ScheduleEntity> filter (UUID productId) {
        Specification<ScheduleEntity> filters = Specification
                .where(productIdInList(productId));

        return filters;
    }

    public static Specification<ScheduleEntity> productIdInList(UUID productId) {
        return (root, query, criteriaBuilder) -> {
            Expression toJsonbArray = criteriaBuilder.function("jsonb_build_array", UUID.class, criteriaBuilder.literal(productId));
            return criteriaBuilder.equal(criteriaBuilder.function("jsonb_contains", String.class, root.get("productIds"), toJsonbArray), true);
        };
    }
}
