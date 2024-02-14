package br.com.petshop.customer.model.dto.request.app;

import lombok.Builder;

@Builder
public record EmailValidateRequest (String emailToken) {
}
