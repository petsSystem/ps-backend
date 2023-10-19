package br.com.petshop.system.employee.model.dto.response;

import br.com.petshop.system.employee.model.enums.EmployeeType;
import br.com.petshop.system.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse implements Serializable {
    private UUID id;
    private EmployeeType type;
    private String name;
    private String cpf;
    private String phone;
    private Address address;
    private Boolean active;
    private List<UUID> companyIds;
    private LocalDateTime createdAt;
}
