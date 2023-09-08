package br.com.petshop.authentication.controller.app;

import br.com.petshop.authentication.service.AuthenticationService;
import br.com.petshop.model.dto.request.SigninRequest;
import br.com.petshop.model.dto.response.JwtAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app/auth")
@RequiredArgsConstructor
public class AppAuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthenticationResponse login (
            @RequestBody SigninRequest request) {
        request.setEmail("app_".concat(request.getEmail()));
        return authenticationService.login(request);
    }
}
