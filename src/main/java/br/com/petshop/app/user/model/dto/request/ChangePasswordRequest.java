package br.com.petshop.app.user.model.dto.request;

import lombok.Builder;

@Builder
public record ChangePasswordRequest (String password) {
}