package br.com.petshop.authentication.controller.app;

import br.com.petshop.authentication.service.AuthenticationService;
import br.com.petshop.authentication.model.dto.request.AuthenticationRequest;
import br.com.petshop.authentication.model.dto.response.AuthenticationResponse;
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

@RestController
@RequestMapping("/api/v1/app/auth")
@Tag(name = "APP Authentication Services")
public class AppAuthenticationController {
    @Autowired private AuthenticationService authenticationService;

    @Operation(summary = "Serviço que efetua login no aplicativo.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário ou senha estão incorretos.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unauthorized\",\n" +
                            "\"status\": 401,\n" +
                            "\"detail\": \"Usuário ou senha estão incorretos.\",\n" +
                            "\"instance\": \"/api/v1/app/token/auth\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao efetuar login. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/token/auth\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse login (
            @RequestBody AuthenticationRequest request) {
        request.setEmail("app_".concat(request.getEmail()));
        return authenticationService.login(request);
    }
}
