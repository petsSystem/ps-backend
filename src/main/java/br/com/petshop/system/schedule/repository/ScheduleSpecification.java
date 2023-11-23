package br.com.petshop.system.schedule.repository;

import br.com.petshop.system.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.system.schedule.model.entity.ScheduleEntity;
import br.com.petshop.system.schedule.model.enums.ScheduleType;
import jakarta.persistence.criteria.Expression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScheduleSpecification {

    public static Specification<ScheduleEntity> filter (ScheduleFilterRequest filter) {
//        Specification<ScheduleEntity> filters = Specification
//                .where(StringUtils.isBlank(filter.getCpf()) ? null : cpfEqual(filter.getCpf()))
//                .and(StringUtils.isBlank(filter.getEmail()) ? null : emailEqual(filter.getEmail()))
//                .and(filter.getType() == null ? null : typeEqual(filter.getType()))
//                .and(filter.getActive() == null ? null : activeEqual(filter.getActive()))
//                .and(filter.getCompanyId() == null ? null : companyIdInList(filter.getCompanyId()));
//
//        return filters;
        return null;
    }

    public static Specification<ScheduleEntity> cpfEqual(String cpf) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("cpf"), cpf);
        };
    }

    public static Specification<ScheduleEntity> emailEqual(String email) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }

    public static Specification<ScheduleEntity> typeEqual(ScheduleType type) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    public static Specification<ScheduleEntity> activeEqual(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }

    public static Specification<ScheduleEntity> companyIdInList(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            Expression toJsonbArray = criteriaBuilder.function("jsonb_build_array", UUID.class, criteriaBuilder.literal(companyId));
            return criteriaBuilder.equal(criteriaBuilder.function("jsonb_contains", String.class, root.get("companyIds"), toJsonbArray), true);
        };
    }
}
