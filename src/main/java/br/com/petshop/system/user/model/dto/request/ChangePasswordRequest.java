package br.com.petshop.system.user.model.dto.request;

import lombok.Builder;

@Builder
public record ChangePasswordRequest (String password) {
}