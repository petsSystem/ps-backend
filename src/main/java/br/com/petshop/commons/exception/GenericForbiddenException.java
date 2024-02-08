package br.com.petshop.commons.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GenericForbiddenException extends RuntimeException{
    public GenericForbiddenException(String message){
            super(message);
        }
}
