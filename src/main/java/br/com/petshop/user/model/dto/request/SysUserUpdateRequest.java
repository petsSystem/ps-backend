package br.com.petshop.user.model.dto.request;

import br.com.petshop.commons.model.Address;
import jakarta.validation.constraints.NotEmpty;
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
 * Classe dto responsável pela atualização dos dados de um usuário do sistema.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserUpdateRequest implements Serializable {
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String phone;
    @NotNull
    @NotEmpty
    private List<UUID> profileIds;
    @NotNull
    private List<UUID> companyIds;
    @NotNull
    private Address address;
}
