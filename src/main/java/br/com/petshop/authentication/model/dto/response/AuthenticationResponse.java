package br.com.petshop.authentication.model.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token) {
}
