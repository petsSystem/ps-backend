package br.com.petshop.system.employee.model.dto.request;

import br.com.petshop.system.company.model.enums.Category;
import br.com.petshop.system.employee.model.enums.EmployeeType;
import br.com.petshop.system.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateRequest implements Serializable {
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private EmployeeType type;
    private Address address;
    private UUID companyId;
    @Builder.Default
    private Boolean active = true;
}
