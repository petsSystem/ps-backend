package br.com.petshop.commons.exception;

import lombok.NoArgsConstructor;

/**
 * Exception responsável por sinalizar informação de senha incorreta.
 */
@NoArgsConstructor
public class GenericIncorrectPasswordException extends RuntimeException{
    public GenericIncorrectPasswordException(String message){
        super(message);
    }
}
