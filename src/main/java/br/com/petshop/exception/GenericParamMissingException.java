package br.com.petshop.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GenericParamMissingException extends RuntimeException{
    public GenericParamMissingException(String message){
            super(message);
        }
}
