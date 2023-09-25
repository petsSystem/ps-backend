package br.com.petshop.app.user.model.dto.request;

import lombok.Builder;

@Builder
public record EmailValidateRequest (String emailToken) {
}
