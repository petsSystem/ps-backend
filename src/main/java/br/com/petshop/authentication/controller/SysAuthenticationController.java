package br.com.petshop.authentication.controller;

import br.com.petshop.authentication.model.dto.request.AuthenticationForget;
import br.com.petshop.authentication.model.dto.request.AuthenticationRequest;
import br.com.petshop.authentication.model.dto.response.AuthenticationResponse;
import br.com.petshop.authentication.model.enums.AuthType;
import br.com.petshop.authentication.service.AuthenticationBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe responsável pelos endpoints de autenticação do sistema web.
 */
@RestController
@RequestMapping("/api/v1/pet/sys/auth")
@Tag(name = "Authentication Services for System")
public class SysAuthenticationController {
    @Autowired private AuthenticationBusinessService businessService;

    @Operation(summary = "Serviço de autenticação no sistema PetHound.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao efetuar login. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/auth\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário ou senha estão incorretos.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unauthorized\",\n" +
                            "\"status\": 401,\n" +
                            "\"detail\": \"Usuário ou senha estão incorretos.\",\n" +
                            "\"instance\": \"/api/v1/sys/auth\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse login(
            @RequestBody AuthenticationRequest request) {
        request.setUsername("sys_".concat(request.getUsername()));
        return businessService.login(request);
    }

    @Operation(summary = "Serviço 'esqueci minha senha'. Envio de email com nova senha para o sistema PetHound.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao enviar email com nova senha. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/auth/forget\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Usuário não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/auth/forget\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping("/forget")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void forget (
            @RequestBody AuthenticationForget request) {
        businessService.forget(request, AuthType.SYS);
    }
}
