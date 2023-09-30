package br.com.petshop.system.employee.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    private String addressPostalCode;
    private String addressStreet;
    private String addressNumber;
    private String addressNeighborhood;
    private String addressCity;
    private String addressState;
    private String addressCountry;

    private Boolean active;
    private LocalDateTime createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updatedAt;
}
