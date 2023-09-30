package br.com.petshop.system.employee.model.dto.request;

import br.com.petshop.system.employee.model.enums.EmployeeType;
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
public class EmployeeCreateRequest implements Serializable {
    private EmployeeType type;
    private String name;
    private String cpf;
    private String phone;

    private String addressPostalCode;
    private String addressStreet;
    private String addressNumber;
    private String addressNeighborhood;
    private String addressCity;
    private String addressState;
    private String addressCountry;

    private String companyId;

    @Builder.Default
    private Boolean active = true;
}
