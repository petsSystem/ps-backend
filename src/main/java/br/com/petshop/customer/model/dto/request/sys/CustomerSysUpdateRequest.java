package br.com.petshop.customer.model.dto.request.sys;

import br.com.petshop.commons.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Classe dto responsável pela atualização de um cliente pelo sistema web.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSysUpdateRequest implements Serializable {
    private String name;
    private String email;
    private String phone;
    private String birthDate;
    private Address address;
}
