package br.com.petshop.customer.model.dto.request.app;

public class ForgetRequestMock {
    public static ForgetRequest get() {
        return ForgetRequest.builder()
                .email("email.do.cliente@email.com")
                .changePassword(false)
                .build();
    }
}
