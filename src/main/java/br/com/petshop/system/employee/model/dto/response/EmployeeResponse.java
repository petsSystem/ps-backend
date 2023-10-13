package br.com.petshop.system.employee.model.dto.response;

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
public class EmployeeResponse implements Serializable {
    private String id;
    private String type;
    private String name;
    private String cpf;
    private String phone;
    private Address address;
    private Boolean active;
    private LocalDateTime createdAt;
}
