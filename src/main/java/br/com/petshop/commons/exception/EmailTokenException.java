package br.com.petshop.commons.exception;

import lombok.NoArgsConstructor;

/**
 * Exception para casos de token expirado, na validação do token por email.
 */
@NoArgsConstructor
public class EmailTokenException extends RuntimeException{
    public EmailTokenException(String message){
            super(message);
        }
}
