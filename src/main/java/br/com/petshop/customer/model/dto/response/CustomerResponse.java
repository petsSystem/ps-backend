package br.com.petshop.customer.model.dto.response;

import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.model.enums.Origin;
import br.com.petshop.commons.model.Address;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Classe dto responsável pelo retorno dos dados de um cliente.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse implements Serializable {
    private UUID id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String birthDate;
    private Origin origin;
    private AppStatus appStatus;
    private Boolean active;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UUID> companyIds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UUID> favorites;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Address address;
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean changePassword;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean emailValidated;
}
