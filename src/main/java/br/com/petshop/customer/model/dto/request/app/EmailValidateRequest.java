package br.com.petshop.customer.model.dto.request.app;

import lombok.Builder;

/**
 * Classe dto responsável pela validação de email via token de um cliente pelo aplicativo.
 */
@Builder
public record EmailValidateRequest (String emailToken) {
}
