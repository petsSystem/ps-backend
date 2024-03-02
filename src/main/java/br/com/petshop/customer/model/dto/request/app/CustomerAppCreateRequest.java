package br.com.petshop.customer.model.dto.request.app;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.model.enums.Origin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Classe dto responsável pela criação de um cliente pelo aplicativo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAppCreateRequest implements Serializable {
    @NotNull
    private String name;
    @NotNull
    private String cpf;
    @NotNull
    private String email;
    private String birthDate;
    @NotNull
    private String password;
    @Builder.Default
    private Origin origin = Origin.APP;
    @Builder.Default
    private AppStatus appStatus = AppStatus.ACTIVE;
    @Builder.Default
    private Boolean active = true;
    @Builder.Default
    private Boolean changePassword = false;
    @Builder.Default
    private Role role = Role.USER;
    @Builder.Default
    private Boolean emailValidated = false;
}
