package br.com.petshop.customer.model.dto.request.app;

import br.com.petshop.commons.model.Address;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Classe dto responsável pela atualização de um cliente pelo aplicativo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAppUpdateRequest implements Serializable {
    @NotNull
    private String name;
    @NotNull
    private String email;
    private String phone;
    private String birthDate;
    private Address address;
}
