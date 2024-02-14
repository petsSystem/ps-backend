package br.com.petshop.customer.model.dto.request.app;

import lombok.Builder;

@Builder
public record ChangePasswordRequest (String password) {
}