package br.com.petshop.system.poc;

import br.com.petshop.system.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PocDto implements Serializable {
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private Boolean active;
    private Address address;
    private List<String> companyIds;
}
