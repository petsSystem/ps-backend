package br.com.petshop.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmailTokenException extends RuntimeException{
    public EmailTokenException(String message){
            super(message);
        }
}
