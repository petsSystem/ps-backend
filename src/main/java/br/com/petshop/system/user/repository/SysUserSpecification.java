package br.com.petshop.system.user.repository;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.system.user.model.dto.request.SysUserFilterRequest;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import jakarta.persistence.criteria.Expression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SysUserSpecification {

    public static Specification<SysUserEntity> filter (SysUserFilterRequest filter) {
        Specification<SysUserEntity> filters = Specification
                .where(StringUtils.isBlank(filter.getEmail()) ? null : emailEqual(filter.getEmail()))
                .and(filter.getEmployeeId() == null ? null : employeeIdEqual(filter.getEmployeeId()))
                .and(filter.getAccessGroupId() == null ? null : accessGroupIdEqual(filter.getAccessGroupId()))
                .and(filter.getRole() == null ? null : roleEqual(filter.getRole()))
                .and(filter.getActive() == null ? null : activeEqual(filter.getActive()))
                .and((filter.getCompanyIds() != null && filter.getCompanyIds().size() > 0) ? companyIdInList(filter.getCompanyIds().get(0)) : null)
                .and((filter.getCompanyIds() != null && filter.getCompanyIds().size() > 1) ? companyIdInList(filter.getCompanyIds().get(1)) : null)
                .and((filter.getCompanyIds() != null && filter.getCompanyIds().size() > 2) ? companyIdInList(filter.getCompanyIds().get(2)) : null)
                .and((filter.getCompanyIds() != null && filter.getCompanyIds().size() > 3) ? companyIdInList(filter.getCompanyIds().get(3)) : null);
        //arrumar solucao para a implementacao acima (array de companyIds: como checar dinamicamente?)

        return filters;
    }

    public static Specification<SysUserEntity> emailEqual(String email) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }

    public static Specification<SysUserEntity> employeeIdEqual(UUID employeeId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("employee").get("id"), employeeId);
        };
    }

    public static Specification<SysUserEntity> accessGroupIdEqual(UUID accessGroupId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("accessGroupId"), accessGroupId);
        };
    }

    public static Specification<SysUserEntity> roleEqual(Role role) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("role"), role);
        };
    }

    public static Specification<SysUserEntity> activeEqual(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }

    public static Specification<SysUserEntity> companyIdInList(UUID companyIds) {
        return (root, query, criteriaBuilder) -> {
            Expression toJsonbArray = criteriaBuilder.function("jsonb_build_array", UUID.class, criteriaBuilder.literal(companyIds));
            return criteriaBuilder.equal(criteriaBuilder.function("jsonb_contains", String.class, root.get("employee").get("companyIds"), toJsonbArray), true);
        };
    }
}
