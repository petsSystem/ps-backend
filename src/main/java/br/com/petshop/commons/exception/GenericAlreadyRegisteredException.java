package br.com.petshop.commons.exception;

import lombok.NoArgsConstructor;

/**
 * Exception responsável por sinalizar entidade já criada.
 */
@NoArgsConstructor
public class GenericAlreadyRegisteredException extends RuntimeException{
    public GenericAlreadyRegisteredException(String message){
            super(message);
        }
}
