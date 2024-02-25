package br.com.petshop.profile.model.dto;

import br.com.petshop.profile.model.enums.Action;
import br.com.petshop.profile.model.enums.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Classe dto responsável pelo retorno das permissões dos perfis do sistema.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable {
    private Resource resource;
    private List<Action> actions;
}
