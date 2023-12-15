package br.com.petshop.system.user.controller;

import br.com.petshop.authentication.model.dto.request.AuthenticationRequest;
import br.com.petshop.authentication.model.dto.response.AuthenticationResponse;
import br.com.petshop.authentication.service.AuthenticationService;
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
@RequestMapping("/api/v1/sys/auth")
@Tag(name = "SYS - Authentication Services")
public class SysAuthenticationController {
    @Autowired
    private AuthenticationService service;

    @Operation(summary = "Serviço que efetua login no sistema Pet System.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao efetuar login. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/web/token/auth\"\n" +
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
                            "\"instance\": \"/api/v1/web/token/auth\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse login(
            @RequestBody AuthenticationRequest request) {
        request.setEmail("sys_".concat(request.getEmail()));
        return service.login(request);
    }
}
