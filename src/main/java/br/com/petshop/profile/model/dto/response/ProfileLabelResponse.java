package br.com.petshop.profile.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Classe dto responsável pelo retorno dos labels de cada perfil existente no sistema.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileLabelResponse implements Serializable {
    private String id;
    private String name;
}
