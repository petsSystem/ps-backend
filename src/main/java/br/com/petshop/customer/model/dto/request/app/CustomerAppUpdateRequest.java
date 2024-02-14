package br.com.petshop.customer.model.dto.request.app;

import br.com.petshop.commons.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAppUpdateRequest implements Serializable {
    private String name;
    private String email;
    private String phone;
    private String birthDate;
    private Address address;
}
