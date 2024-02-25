package br.com.petshop.authentication.model.dto.response;

import lombok.Builder;

/**
 * Classe dto responsável pelo retorno do token de autenticação.
 */
@Builder
public record AuthenticationResponse(String token) {
}
