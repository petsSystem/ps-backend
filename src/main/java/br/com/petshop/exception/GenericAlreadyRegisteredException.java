package br.com.petshop.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GenericAlreadyRegisteredException extends RuntimeException{
    public GenericAlreadyRegisteredException(String message){
            super(message);
        }
}
