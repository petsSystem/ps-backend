package br.com.petshop.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GenericNotFoundException extends RuntimeException{
    public GenericNotFoundException(String message){
            super(message);
        }
}
