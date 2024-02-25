package br.com.petshop.company.model.dto.response;

import br.com.petshop.commons.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe dto responsável pelo retorno dos dados de uma loja/petshop.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse implements Serializable {
    private UUID id;
    private String name;
    private String cnpj;
    private String phone;
    private Address address;
    private Boolean active;
}
