package br.com.petshop.system.user.model.dto.request;

import br.com.petshop.system.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserUpdateRequest implements Serializable {
    private String name;
    private String phone;
    private String type;
    private Address address;
}
