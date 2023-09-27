package br.com.petshop.system.user.controller;

import br.com.petshop.system.user.model.dto.request.SysChangePasswordRequest;
import br.com.petshop.system.user.model.dto.request.SysEmailValidateRequest;
import br.com.petshop.system.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.system.user.model.dto.request.SysUserUpdateRequest;
import br.com.petshop.system.user.model.dto.response.SysUserResponse;
import br.com.petshop.system.user.service.SysUserService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/sys/users")
@Tag(name = "System Users Services")
public class SysUserController {
    @Autowired private SysUserService systemUserService;

    @Operation(summary = "Serviço que cria usuário no Sistema PetShop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "422",
                    description = "Usuário já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Unprocessable Entity\",\n" +
                            "    \"status\": 422,\n" +
                            "    \"detail\": \"Email já cadastrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/users\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/users\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public SysUserResponse create (
            @RequestBody SysUserCreateRequest request) {
        return systemUserService.create(request);
    }

    @Operation(summary = "Serviço de alteração dos dados do usuário no Sistema PetShop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/users\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public SysUserResponse update (
            Principal authentication,
            @RequestBody SysUserUpdateRequest request) {
        return systemUserService.update(authentication, request);
    }


    @Operation(summary = "Serviço para recuperar dados do cadastro do usuário no Sistema PetShop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao retornar dados do usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/users\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public SysUserResponse get (
            Principal authentication) {
        return systemUserService.getByEmail(authentication);
    }

    @Operation(summary = "Serviço de validação do email do usuário no Sistema PetShop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "412",
                    description = "Usuário digita token incorreto.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Precondition Failed\",\n" +
                            "    \"status\": 412,\n" +
                            "    \"detail\": \"Token inválido.\",\n" +
                            "    \"instance\": \"/api/v1/system/users/email/validate\"\n" +
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
                            "    \"instance\": \"/api/v1/system/users/email/validate\"\n" +
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
                            "\"instance\": \"/api/v1/system/users/email/validate\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PatchMapping("/email/validate")
    @ResponseStatus(HttpStatus.OK)
    public SysUserResponse emailValidate (
            Principal authentication,
            @RequestBody SysEmailValidateRequest request) {
        return systemUserService.emailValidate(authentication, request);
    }

    @Operation(summary = "Serviço de reenvio de token para validação do email do usuário no Sistema PetShop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao reenviar email para validação. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/users/email/validate/resend\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @GetMapping("/email/validate/resend")
    @ResponseStatus(HttpStatus.OK)
    public SysUserResponse emailValidateResend (
            Principal authentication) {
        return systemUserService.emailValidateResend(authentication);
    }

    @Operation(summary = "Serviço que altera senha do usuário no Sistema PetShop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao trocar senha. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/users/password\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword (
            Principal authentication,
            @RequestBody SysChangePasswordRequest request) {
        systemUserService.changePassword(authentication, request);
    }

    @Operation(summary = "Serviço para desativar usuário no Sistema PetShop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao desativar usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/users/{email}/deactivate\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public void deactivate (
            @PathVariable ("email") String email) {
        systemUserService.deactivate(email);
    }
}
