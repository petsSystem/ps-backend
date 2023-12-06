package br.com.petshop.system.company.model.dto.response;

import br.com.petshop.system.company.model.enums.Category;
import br.com.petshop.system.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private List<Category> categories;
    private List<UUID> scheduleIds;
    private LocalDateTime createdAt;
}
