package br.com.petshop.system.employee.model.dto.request;

import br.com.petshop.system.employee.model.enums.EmployeeType;
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
public class EmployeeFilterRequest implements Serializable {
    private UUID companyId;
    private String cpf;
    private String email;
    private EmployeeType type;
    private Boolean active;
}
