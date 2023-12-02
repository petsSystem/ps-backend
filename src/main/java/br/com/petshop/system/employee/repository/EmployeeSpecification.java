package br.com.petshop.system.employee.repository;

import br.com.petshop.system.employee.model.dto.request.EmployeeFilterRequest;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import jakarta.persistence.criteria.Expression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeSpecification {

    public static Specification<EmployeeEntity> filter (EmployeeFilterRequest filter) {
        Specification<EmployeeEntity> filters = Specification
                .where(StringUtils.isBlank(filter.getCpf()) ? null : cpfEqual(filter.getCpf()))
                .and(StringUtils.isBlank(filter.getEmail()) ? null : emailEqual(filter.getEmail()))
//                .and(filter.getType() == null ? null : typeEqual(filter.getType()))
                .and(filter.getActive() == null ? null : activeEqual(filter.getActive()))
                .and(filter.getCompanyId() == null ? null : companyIdInList(filter.getCompanyId()));

        return filters;
    }

    public static Specification<EmployeeEntity> cpfEqual(String cpf) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("cpf"), cpf);
        };
    }

    public static Specification<EmployeeEntity> emailEqual(String email) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }

    public static Specification<EmployeeEntity> typeEqual(String type) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    public static Specification<EmployeeEntity> activeEqual(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }

    public static Specification<EmployeeEntity> companyIdInList(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            Expression toJsonbArray = criteriaBuilder.function("jsonb_build_array", UUID.class, criteriaBuilder.literal(companyId));
            return criteriaBuilder.equal(criteriaBuilder.function("jsonb_contains", String.class, root.get("companyIds"), toJsonbArray), true);
        };
    }
}
