package br.com.petshop.commons.exception;

import lombok.NoArgsConstructor;

/**
 * Exception responsável por sinalizar falta de parâmetro para execução de uma tarefa.
 */
@NoArgsConstructor
public class GenericParamMissingException extends RuntimeException{
    public GenericParamMissingException(String message){
            super(message);
        }
}
