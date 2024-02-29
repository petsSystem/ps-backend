package br.com.petshop.company.model.dto.request;

import br.com.petshop.commons.model.Address;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Classe dto responsável pela criação de uma loja/petshop.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreateRequest implements Serializable {
    @NotNull
    private String name;
    @NotNull
    private String cnpj;
    private String phone;
    private Address address;
    @Builder.Default
    private Boolean active = false;
}
