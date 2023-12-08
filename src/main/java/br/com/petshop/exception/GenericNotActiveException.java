package br.com.petshop.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GenericNotActiveException extends RuntimeException{
    public GenericNotActiveException(String message){
        super(message);
    }
}
