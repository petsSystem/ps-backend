package br.com.petshop.user.app.model.dto.request;

import lombok.Builder;

@Builder
public record EmailValidateRequest (String emailToken) {
}
