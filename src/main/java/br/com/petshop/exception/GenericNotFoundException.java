package br.com.petshop.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GenericNotFoundException extends RuntimeException{
    public GenericNotFoundException(String message){
            super(message);
        }
}
