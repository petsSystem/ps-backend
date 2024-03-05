package br.com.petshop.customer.controller;

import br.com.petshop.customer.model.dto.request.app.CustomerAppUpdateRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.dto.request.app.EmailValidateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.service.app.CustomerAppBusinessService;
import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Classe responsável pelos endpoints de clientes do aplicativo.
 */
@RestController
@RequestMapping("/api/v1/pet/app/customers")
@Tag(name = "Serviços para Clientes")
public class CustomerAPPController {
    @Autowired private CustomerAppBusinessService businessService;

    @Operation(summary = "Serviço de alteração dos dados do cliente autenticado na aplicação.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/app/customers\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse update (
            Principal authentication,
            @RequestBody @Valid CustomerAppUpdateRequest request) {
        return businessService.update(authentication, request);
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
                            "\"detail\": \"Erro ao atualizar senha do cliente. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/app/customers/password\"\n" +
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
                            "    \"instance\": \"/api/v1/pet/app/customers/password\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse changePassword (
            Principal authentication,
            @RequestBody CustomerChangePasswordRequest request) {
        return businessService.changePassword(authentication, request);
    }

    @Operation(summary = "Serviço para recuperar dados do cadastro do cliente autenticado no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao retornar dados do cliente. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/app/customers\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse me (
            Principal authentication) {
        return businessService.me(authentication);
    }

    @Operation(summary = "Serviço para favoritar petshop para cliente.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao associar petshop ao cliente. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/customers/{customerId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/favorite", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse favorite (
            Principal authentication,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"add\",\n" +
                    "        \"path\": \"/companyIds/0\",\n" +
                    "        \"value\": \"${companyId}\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return businessService.favorite(authentication, patch);
    }

    @Operation(summary = "Serviço para desfavoritar petshop para cliente.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao desassociar petshop de cliente. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/customers/{customerId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/unfavorite", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse unfavorite (
            Principal authentication,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"remove\",\n" +
                    "        \"path\": \"/companyIds/0\",\n" +
                    "        \"value\": \"${companyId}\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return businessService.unfavorite(authentication, patch);
    }

    @Operation(summary = "Serviço de validação do email do cliente no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao validar email. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/app/customers/email/validate\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário informa token expirado (após uma hora do envio do email).",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Unauthorized\",\n" +
                            "    \"status\": 401,\n" +
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
                            "    \"instance\": \"/api/v1/pet/app/customers/email/validate\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/email/validate")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse emailValidate (
            Principal authentication,
            @RequestBody EmailValidateRequest request) {
        return businessService.emailValidate(authentication, request);
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
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resendEmailValidate (
            Principal authentication) {
        businessService.resendEmailValidate(authentication);
    }

    @Operation(summary = "Serviço de cancelamento/desativação do cliente no aplicativo.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cancelar conta. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/customers/{customerId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/deactivate", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deactivate (
            Principal authentication,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"active\",\n" +
                    "        \"value\": \"false\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        businessService.deactivate(authentication, patch);
    }
}
