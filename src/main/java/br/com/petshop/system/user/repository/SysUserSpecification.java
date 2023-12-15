package br.com.petshop.system.user.repository;

import br.com.petshop.system.user.model.entity.SysUserEntity;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SysUserSpecification {

    public static Specification<SysUserEntity> filter (UUID companyId) {
        Specification<SysUserEntity> filters = Specification
                .where(companyIdInList(companyId));

        return filters;
    }

//    public static Specification<EmployeeEntity> activeEqual(Boolean active) {
//        return (root, query, criteriaBuilder) -> {
//            return criteriaBuilder.equal(root.get("active"), active);
//        };
//    }

    public static Specification<SysUserEntity> companyIdInList(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            Expression toJsonbArray = criteriaBuilder.function("jsonb_build_array", UUID.class, criteriaBuilder.literal(companyId));
            return criteriaBuilder.equal(criteriaBuilder.function("jsonb_contains", String.class, root.get("companyIds"), toJsonbArray), true);
        };
    }
}
