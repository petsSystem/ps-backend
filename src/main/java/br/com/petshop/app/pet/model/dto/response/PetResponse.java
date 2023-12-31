package br.com.petshop.app.pet.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse implements Serializable {
    private String id;
    private String name; //nome
    private String gender; //sexo
    private String species; //especie
    private String dateBirth; //data de nascimento
    private String size; //porte
    private Boolean neutered; //castrado
    private String  temper; //temperamento
    private String  coat; //pelagem
    private String  weight; //peso
    private String  breed; //raça
    private Boolean active;
    private LocalDateTime createdAt;
}
