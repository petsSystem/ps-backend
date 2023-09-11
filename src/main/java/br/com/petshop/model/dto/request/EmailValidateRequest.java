package br.com.petshop.model.dto.request;

import lombok.Builder;

@Builder
public record EmailValidateRequest (String emailToken) {
}
