package br.com.petshop.user.model.dto.request;

import lombok.Builder;

@Builder
public record SysUserForgetRequest(String email) {
}