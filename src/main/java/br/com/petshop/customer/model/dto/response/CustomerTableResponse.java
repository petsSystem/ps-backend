package br.com.petshop.customer.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe dto responsável pelo retorno de dados resumidos de um cliente para apresentação em tela/lista.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTableResponse implements Serializable {
    private UUID id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private Boolean active;
    private Boolean favorite;
}
