package br.com.petshop.commons.exception;

import lombok.NoArgsConstructor;

/**
 * Exception responsável por bloquear acessos indevidos.
 */
@NoArgsConstructor
public class GenericForbiddenException extends RuntimeException{
    public GenericForbiddenException(String message){
            super(message);
        }
}
