package br.com.petshop.user.model.dto.response;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.profile.model.dto.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Classe dto responsável pelo retorno dos dados de um usuário do sistema.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserMeResponse implements Serializable {
    private UUID id;
    private String name;
    private String username;
    private Boolean changePassword;
    private Role role;
    private UUID companyId;
    private String companyName;
    private UUID currentCompanyId;
    private List<Permission> permissions;
}
