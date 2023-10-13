package br.com.petshop.system.employee.model.dto.request;

import br.com.petshop.system.employee.model.enums.EmployeeType;
import br.com.petshop.system.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateRequest implements Serializable {
    private String name;
    private String phone;
    private EmployeeType type;
    private Address address;
}
