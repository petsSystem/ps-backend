package br.com.petshop.app.user.controller;

import br.com.petshop.app.user.model.dto.request.AppUserUpdateRequest;
import br.com.petshop.app.user.model.dto.response.AppUserResponse;
import br.com.petshop.app.user.service.AppUserService;
import br.com.petshop.app.user.model.dto.request.ChangePasswordRequest;
import br.com.petshop.app.user.model.dto.request.EmailValidateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/app/users")
@Tag(name = "App Users Services")
public class AppUserController {
    @Autowired private AppUserService userService;

    @Operation(summary = "Serviço de alteração dos dados do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/users\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public AppUserResponse update (
            Principal authentication,
            @RequestBody AppUserUpdateRequest request) {
        return userService.update(authentication, request);
    }


    @Operation(summary = "Serviço para recuperar dados do cadastro do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao retornar dados do usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/users\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public AppUserResponse get (
            Principal authentication) {
        return userService.getByEmail(authentication);
    }

    @Operation(summary = "Serviço de validação do email do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "412",
                    description = "Usuário digita token incorreto.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Precondition Failed\",\n" +
                            "    \"status\": 412,\n" +
                            "    \"detail\": \"Token inválido.\",\n" +
                            "    \"instance\": \"/api/v1/app/users/email/validate\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário informa token expirado (após uma hora do envio do email).",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Forbidden\",\n" +
                            "    \"status\": 403,\n" +
                            "    \"detail\": \"Token expirado. Solicite novo token.\",\n" +
                            "    \"instance\": \"/api/v1/app/users/email/validate\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao validar email. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/users/email/validate\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PatchMapping("/email/validate")
    @ResponseStatus(HttpStatus.OK)
    public AppUserResponse emailValidate (
            Principal authentication,
            @RequestBody EmailValidateRequest request) {
        return userService.emailValidate(authentication, request);
    }

    @Operation(summary = "Serviço de reenvio de token para validação do email do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao reenviar email para validação. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/users/email/validate/resend\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PatchMapping("/email/validate/resend")
    @ResponseStatus(HttpStatus.OK)
    public AppUserResponse emailValidateResend (
            Principal authentication) {
        return userService.emailValidateResend(authentication);
    }

    @Operation(summary = "Serviço que altera senha do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao trocar senha. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/users/password\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword (
            Principal authentication,
            @RequestBody ChangePasswordRequest request) {
        userService.changePassword(authentication, request);
    }

    @Operation(summary = "Serviço para desativar usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao desativar usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1app/users/{email}/deactivate\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public void deactivate (
            @PathVariable ("email") String email) {
        userService.deactivate(email);
    }
}
