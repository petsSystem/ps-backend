package br.com.petshop.model.dto.request;

import lombok.Builder;

@Builder
public record ChangePasswordRequest (String password) {
}