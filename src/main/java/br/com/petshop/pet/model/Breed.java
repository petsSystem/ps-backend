package br.com.petshop.pet.model;

import br.com.petshop.pet.model.enums.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Breed implements Serializable {
    private String name;
    private Size size;
}
