package br.com.petshop.profile.model.dto.request;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.profile.model.dto.Permission;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Classe dto responsável pela criação de um perfil do sistema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCreateRequest implements Serializable {
    @NotNull
    private String name;
    private Role role;
    private List<Permission> permissions;
}
