package br.com.petshop.commons.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GenericIncorrectPasswordException extends RuntimeException{
    public GenericIncorrectPasswordException(String message){
        super(message);
    }
}
