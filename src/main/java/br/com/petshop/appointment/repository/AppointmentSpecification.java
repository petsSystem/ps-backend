package br.com.petshop.appointment.repository;

import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequest;
import br.com.petshop.appointment.model.entity.AppointmentEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AppointmentSpecification {

    public static Specification<AppointmentEntity> filter (AppointmentFilterRequest filter) {
        Specification<AppointmentEntity> filters = Specification
                .where(productIdEqual(filter.getProductId()))
                .and(userIdEqual(filter.getUserId() == null ? null : filter.getUserId()));

        return filters;
    }

    public static Specification<AppointmentEntity> companyIdEqual(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("companyId"), companyId);
        };
    }

    public static Specification<AppointmentEntity> userIdEqual(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("userId"), userId);
        };
    }

    public static Specification<AppointmentEntity> productIdEqual(UUID productId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("productId"), productId);
        };
    }
}
