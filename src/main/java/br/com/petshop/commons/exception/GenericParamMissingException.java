package br.com.petshop.commons.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GenericParamMissingException extends RuntimeException{
    public GenericParamMissingException(String message){
            super(message);
        }
}
