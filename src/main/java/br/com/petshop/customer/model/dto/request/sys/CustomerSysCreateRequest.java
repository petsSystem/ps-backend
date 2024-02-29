package br.com.petshop.customer.model.dto.request.sys;

import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.model.enums.Origin;
import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.commons.model.Address;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe dto responsável pela criação de um cliente pelo sistema web.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSysCreateRequest implements Serializable {
    @NotNull
    private String name;
    @NotNull
    private String cpf;
    @NotNull
    private String email;
    private String phone;
    private String birthDate;
    @Builder.Default
    private Origin origin = Origin.SYS;
    @Builder.Default
    private AppStatus appStatus = AppStatus.PENDING;
    @Builder.Default
    private Boolean active = true;

    private UUID companyId;
    private Address address;

    @Builder.Default
    private Boolean changePassword = false;
    @Builder.Default
    private Role role = Role.USER;

}
