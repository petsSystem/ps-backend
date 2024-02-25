package br.com.petshop.configuration;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Classe de configuração de lançamento de exceções.
 */
@ControllerAdvice
public class ExceptionHandler
        extends ResponseEntityExceptionHandler {

}