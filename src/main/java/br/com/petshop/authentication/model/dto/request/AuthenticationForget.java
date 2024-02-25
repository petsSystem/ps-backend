package br.com.petshop.authentication.model.dto.request;

/**
 * Classe dto responsável pela informação de um username para envio de email de esquecimento.
 */
public record AuthenticationForget(String username) {
}
