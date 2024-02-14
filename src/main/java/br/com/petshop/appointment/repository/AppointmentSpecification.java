package br.com.petshop.appointment.repository;

import br.com.petshop.appointment.model.entity.AppointmentEntity;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AppointmentSpecification {

    public static Specification<AppointmentEntity> filter (UUID productId) {
        Specification<AppointmentEntity> filters = Specification
                .where(productIdInList(productId));

        return filters;
    }

    public static Specification<AppointmentEntity> productIdInList(UUID productId) {
        return (root, query, criteriaBuilder) -> {
            Expression toJsonbArray = criteriaBuilder.function("jsonb_build_array", UUID.class, criteriaBuilder.literal(productId));
            return criteriaBuilder.equal(criteriaBuilder.function("jsonb_contains", String.class, root.get("productIds"), toJsonbArray), true);
        };
    }
}
