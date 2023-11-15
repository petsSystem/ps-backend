package br.com.petshop.system.company.model.dto.request;

import br.com.petshop.system.company.model.enums.Category;
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
public class CompanyUpdateRequest implements Serializable {
    private String name;
    private String phone;
    private Address address;
    private List<Category> categories;
}
