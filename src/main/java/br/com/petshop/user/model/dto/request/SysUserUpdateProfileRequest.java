package br.com.petshop.user.model.dto.request;

import br.com.petshop.commons.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Classe dto responsável pela atualização de perfil de um usuário do sistema.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserUpdateProfileRequest implements Serializable {
    private String name;
    private String email;
    private String phone;
    private Address address;
}
