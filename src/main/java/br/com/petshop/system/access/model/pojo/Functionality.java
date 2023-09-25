package br.com.petshop.system.access.model.pojo;

import br.com.petshop.system.access.model.enums.AccessType;
import br.com.petshop.system.access.model.enums.FunctionalityName;
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
public class Functionality implements Serializable {
    private FunctionalityName name;
    private List<AccessType> accessesType;
}
