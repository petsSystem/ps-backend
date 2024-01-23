package br.com.petshop.customer.controller;

import br.com.petshop.customer.model.dto.request.AppUserUpdateRequest;
import br.com.petshop.customer.model.dto.request.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.dto.request.CustomerSysCreateRequest;
import br.com.petshop.customer.model.dto.request.EmailValidateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.service.CustomerService;
import br.com.petshop.customer.service.CustomerFacade;
import br.com.petshop.company.model.dto.response.CompanySummaryResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pet/customers")
@Tag(name = "Serviços para Clientes")
public class CustomerController {
    @Autowired private CustomerService userService;
    @Autowired private CustomerFacade facade;

    @Operation(summary = "Serviço de inclusão de cliente no sistema pela loja.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar cliente. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/customers\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Cliente já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Cliente já cadastrado.\",\n" +
                            "\"instance\": \"/api/v1/pet/customers\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(
            @RequestBody CustomerSysCreateRequest request) {
        return facade.createSys(request);
    }

    @Operation(summary = "Serviço de alteração de senha no aplicativo.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse (
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar senha do usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/customers/password\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Senha atual está incorreta.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Bad Request\",\n" +
                            "    \"status\": 400,\n" +
                            "    \"detail\": \"Senha atual está incorreta.\",\n" +
                            "    \"instance\": \"/api/v1/pet/customers/password\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse changePassword (
            Principal authentication,
            @RequestBody CustomerChangePasswordRequest request) {
        return facade.changePassword(authentication, request);
    }

    @Operation(summary = "Serviço de validação do email do usuário no APP.")
    @ApiResponses(value = {
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
                    responseCode = "412",
                    description = "Usuário digita token incorreto.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Precondition Failed\",\n" +
                            "    \"status\": 412,\n" +
                            "    \"detail\": \"Token inválido.\",\n" +
                            "    \"instance\": \"/api/v1/app/users/email/validate\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/email/validate")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse emailValidate (
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
    public CustomerResponse emailValidateResend (
            Principal authentication) {
        return userService.emailValidateResend(authentication);
    }

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
    public CustomerResponse update (
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
    public CustomerResponse get (
            Principal authentication) {
        return userService.getByEmail(authentication);
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
                            "\"detail\": \"Erro ao excluir dados do usuário. Tente novamente mais tarde.\",\n" +
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
