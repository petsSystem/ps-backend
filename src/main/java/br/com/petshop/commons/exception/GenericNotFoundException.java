package br.com.petshop.commons.exception;

import lombok.NoArgsConstructor;

/**
 * Exception responsável por sinalizar entidade não encontrada.
 */
@NoArgsConstructor
public class GenericNotFoundException extends RuntimeException{
    public GenericNotFoundException(String message){
            super(message);
        }
}
