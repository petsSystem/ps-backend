package br.com.petshop.model.dto.response;

import lombok.Builder;

@Builder
public record JwtAuthenticationResponse(String token) {
}
