package br.com.petshop.appointment.repository;

import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequest;
import br.com.petshop.appointment.model.entity.AppointmentEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Classe de queries filtro do agendamento
 */
@Service
public class AppointmentSpecification {

    public static Specification<AppointmentEntity> filter (AppointmentFilterRequest filter) {
        Specification<AppointmentEntity> filters = Specification
                .where(companyIdEqual(filter.getCompanyId()))
                .and(categoryIdEqual(filter.getCategoryId()))
                .and(productIdEqual(filter.getProductId()))
                .and(userIdEqual(filter.getUserId()))
                .and(dateEqual(filter.getDate()));

        return filters;
    }

    public static Specification<AppointmentEntity> companyIdEqual(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("companyId"), companyId);
        };
    }
    public static Specification<AppointmentEntity> categoryIdEqual(UUID categoryId) {
        if (categoryId == null) return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("categoryId"), categoryId);
        };
    }

    public static Specification<AppointmentEntity> productIdEqual(UUID productId) {
        if (productId == null) return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("productId"), productId);
        };
    }

    public static Specification<AppointmentEntity> userIdEqual(UUID userId) {
        if (userId == null) return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("userId"), userId);
        };
    }

    public static Specification<AppointmentEntity> dateEqual(LocalDate date) {
        if (date == null) return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("date"), date);
        };
    }
}
