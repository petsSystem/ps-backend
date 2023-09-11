package br.com.petshop.user.controller.app;

import br.com.petshop.model.dto.request.AddressRequest;
import br.com.petshop.model.dto.request.AppUserUpdateRequest;
import br.com.petshop.model.dto.request.ChangePasswordRequest;
import br.com.petshop.model.dto.request.EmailValidateRequest;
import br.com.petshop.model.dto.response.AppUserResponse;
import br.com.petshop.user.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/app/users")
@RequiredArgsConstructor
public class AppUserController {
    @Autowired private AppUserService userService;

    @PatchMapping("/email/validate")
    @ResponseStatus(HttpStatus.OK)
    public AppUserResponse emailValidate(
            Principal authentication,
            @RequestBody EmailValidateRequest request) {
        return userService.emailValidate(authentication, request);
    }

    @GetMapping("/email/validate/resend")
    @ResponseStatus(HttpStatus.OK)
    public AppUserResponse emailValidateResend(
            Principal authentication) {
        return userService.emailValidateResend(authentication);
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(
            Principal authentication,
            @RequestBody ChangePasswordRequest request) {
        userService.changePassword(authentication, request);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    public AppUserResponse update(
            Principal authentication,
            @RequestBody AppUserUpdateRequest request) {
        return userService.update(authentication, request);
    }

    @PatchMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AppUserResponse address(
            Principal authentication,
            @RequestBody AddressRequest request) {
        return userService.address(authentication, request);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public AppUserResponse get(
            Principal authentication) {
        return userService.getByEmail(authentication);
    }

    @GetMapping("/{email}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public void deactivate(
            @PathVariable ("email") String email) {
        userService.deactivate(email);
    }
}
