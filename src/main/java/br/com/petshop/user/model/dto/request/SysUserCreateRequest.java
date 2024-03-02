package br.com.petshop.user.model.dto.request;

import br.com.petshop.commons.model.Address;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Classe dto responsável pela criação de um usuário do sistema.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserCreateRequest implements Serializable {
    @NotNull
    private String name;
    @NotNull
    private String cpf;
    @NotNull
    private String email;
    private String phone;
    @NotNull
    private List<UUID> profileIds;
    @NotNull
    private List<UUID> companyIds;
    private Address address;
    @Builder.Default
    private Boolean active = true;
    @Builder.Default
    private Boolean changePassword = true;
}
