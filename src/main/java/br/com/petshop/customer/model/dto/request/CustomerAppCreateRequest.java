package br.com.petshop.customer.model.dto.request;

import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.model.enums.Origin;
import br.com.petshop.authentication.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAppCreateRequest implements Serializable {
    private String name;
    private String cpf;
    private String email;
    private String birthDate;
    @Builder.Default
    private Origin origin = Origin.APP;
    @Builder.Default
    private AppStatus appStatus = AppStatus.ACTIVE;
    @Builder.Default
    private Boolean active = true;

    private String password;
    @Builder.Default
    private Boolean changePassword = false;
    @Builder.Default
    private Role role = Role.USER;
    @Builder.Default
    private Boolean emailValidated = false;
}
