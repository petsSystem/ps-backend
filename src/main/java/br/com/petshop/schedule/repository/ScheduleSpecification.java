package br.com.petshop.schedule.repository;

import br.com.petshop.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.schedule.model.entity.ScheduleEntity;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Classe de queries filtro da agenda
 */
@Service
public class ScheduleSpecification {

    public static Specification<ScheduleEntity> filter (ScheduleFilterRequest filter) {
        Specification<ScheduleEntity> filters = Specification
                .where(companyIdEqual(filter.getCompanyId()))
                .and(productIdInList(filter.getProductId()))
                .and(userIdEqual(filter.getUserId()));

        return filters;
    }

    public static Specification<ScheduleEntity> companyIdEqual(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("companyId"), companyId);
        };
    }

    public static Specification<ScheduleEntity> userIdEqual(UUID userId) {
        if (userId == null) return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("userId"), userId);
        };
    }
    public static Specification<ScheduleEntity> productIdInList(UUID productId) {
        if (productId == null) return null;
        return (root, query, criteriaBuilder) -> {
            Expression toJsonbArray = criteriaBuilder.function("jsonb_build_array", UUID.class, criteriaBuilder.literal(productId));
            return criteriaBuilder.equal(criteriaBuilder.function("jsonb_contains", String.class, root.get("productIds"), toJsonbArray), true);
        };
    }
}
