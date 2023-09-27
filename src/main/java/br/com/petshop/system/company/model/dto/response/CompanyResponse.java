package br.com.petshop.system.company.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse implements Serializable {
    private String id;
    private String name;
    private String cnpj;
    private Boolean active;
    private LocalDateTime createdAt;
}
