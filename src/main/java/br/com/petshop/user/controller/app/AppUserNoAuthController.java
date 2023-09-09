package br.com.petshop.user.controller.app;

import br.com.petshop.model.dto.request.AppUserCreateRequest;
import br.com.petshop.model.dto.response.AppUserResponse;
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
@RequestMapping("/api/v1/app/noauth/user")
@RequiredArgsConstructor
public class AppUserNoAuthController {
    @Autowired private AppUserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public AppUserResponse signup(
            @RequestBody AppUserCreateRequest request) {
        return userService.create(request);
    }

    @GetMapping("/{email}/forget")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void forget (
            @PathVariable("email") String email) {
        userService.checkForget(email);
    }
}
