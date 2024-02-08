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

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserCreateRequest implements Serializable {
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private List<UUID> profileIds;
    private List<UUID> companyIds;
    private List<UUID> productIds;
    private Address address;
    @Builder.Default
    private Boolean active = true;
}
