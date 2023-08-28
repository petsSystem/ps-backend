package br.com.petshop.user.controller.app;

import br.com.petshop.dao.entity.AppUserEntity;
import br.com.petshop.model.dto.request.AppUserRequest;
import br.com.petshop.model.dto.response.JwtAuthenticationResponse;
import br.com.petshop.user.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app/user")
@RequiredArgsConstructor
public class AppUserController {
    @Autowired private AppUserService userService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthenticationResponse signup(
            @RequestBody AppUserRequest request) {
        return userService.create(request);
    }

    @GetMapping("/get/{email}")
    @ResponseStatus(HttpStatus.OK)
    public AppUserEntity get(@PathVariable ("email") String email) {
        return userService.findByEmail(email);
    }
}
