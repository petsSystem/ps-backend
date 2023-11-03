package br.com.petshop.system.access.model.dto;

import br.com.petshop.system.access.model.enums.Action;
import br.com.petshop.system.access.model.enums.Resource;
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
public class Permission implements Serializable {
    private Resource resource;
    private List<Action> actions;
}
