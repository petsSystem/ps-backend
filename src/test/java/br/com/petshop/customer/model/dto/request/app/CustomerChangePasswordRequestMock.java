package br.com.petshop.customer.model.dto.request.app;

public class CustomerChangePasswordRequestMock {
    public static CustomerChangePasswordRequest get() {
        return CustomerChangePasswordRequest.builder()
                .oldPassword("123456")
                .newPassword("123456")
                .build();
    }
}
