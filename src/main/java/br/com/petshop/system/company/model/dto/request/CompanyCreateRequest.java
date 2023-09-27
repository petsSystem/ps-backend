package br.com.petshop.system.company.model.dto.request;

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
public class CompanyCreateRequest implements Serializable {
    private String name;
    private String cnpj;
    @Builder.Default
    private Boolean active = true;
}
