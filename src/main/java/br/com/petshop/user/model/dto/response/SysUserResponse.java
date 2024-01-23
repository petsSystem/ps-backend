package br.com.petshop.user.model.dto.response;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.model.Address;
import br.com.petshop.profile.model.dto.response.ProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserResponse implements Serializable {
    private UUID id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private Boolean active;
    private Address address;
    private List<UUID> companyIds;
    private List<UUID> profileIds;
    private Role role;
    private String username;
    private Boolean changePassword;
    private UUID currentCompanyId;
    private List<ProfileResponse> profiles;
}
