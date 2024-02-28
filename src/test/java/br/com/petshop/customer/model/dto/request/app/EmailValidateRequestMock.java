package br.com.petshop.customer.model.dto.request.app;

public record EmailValidateRequestMock(String emailToken) {
    public static EmailValidateRequest get() {
        return new EmailValidateRequest("email.do.cliente@email.com");
    }
}
