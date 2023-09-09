package br.com.petshop.authentication.controller.web;

import br.com.petshop.model.dto.response.JwtAuthenticationResponse;
import br.com.petshop.model.dto.request.SigninRequest;
import br.com.petshop.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/web/token/auth")
@RequiredArgsConstructor
public class WebAuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthenticationResponse login(
            @RequestBody SigninRequest request) {
        request.setEmail("web_".concat(request.getEmail()));
        return authenticationService.login(request);
    }
}
