package br.com.petshop.system.employee.model.entity;

import br.com.petshop.system.employee.model.enums.EmployeeType;
import jdk.jfr.EventType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeSpecification {

    public Specification<EmployeeEntity> companyId(UUID companyId) {
//        return (root, query, builder) -> builder.in(root.get("companyIds"), List.of(companyId));
        return null;
    }

    public Specification<EmployeeEntity> cpf(String cpf) {
        return (root, query, builder) -> builder.equal(root.get("cpf"), cpf);
    }

    public Specification<EmployeeEntity> type(EmployeeType type) {
        return (root, query, builder) -> builder.equal(root.get("type"), type);
    }

    public Specification<EmployeeEntity> active(Boolean active) {
        return (root, query, builder) -> builder.equal(root.get("active"), active);
    }
}
