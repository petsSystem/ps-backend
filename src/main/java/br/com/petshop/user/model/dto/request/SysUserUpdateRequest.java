package br.com.petshop.user.model.dto.request;

import br.com.petshop.commons.model.Address;
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
    private String name;
    private String email;
    private String phone;
    private List<UUID> profileIds;
    private List<UUID> companyIds;
    private Address address;
}
