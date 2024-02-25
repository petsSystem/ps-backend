package br.com.petshop.profile.model.dto.response;

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
 * Classe dto responsável pelo retorno dos dados de um perfil do sistema.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse implements Serializable {
    private UUID id;
    private String name;
    private Role role;
    private List<Permission> permissions;
}
