package br.com.petshop.customer.model.dto.request;

import lombok.Builder;

@Builder
public record ChangePasswordRequest (String password) {
}