package br.com.petshop.app.user.controller;

import br.com.petshop.app.user.model.dto.request.AppUserCreateRequest;
import br.com.petshop.app.user.model.dto.request.AppUserForgetRequest;
import br.com.petshop.app.user.model.dto.response.AppUserResponse;
import br.com.petshop.app.user.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app/noauth/users")
@RequiredArgsConstructor
@Tag(name = "App Users Services (NO AUTH)")
public class AppUserNoAuthController {
    @Autowired private AppUserService userService;

    @Operation(summary = "Serviço que cria usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/noauth/users\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Cliente já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Unprocessable Entity\",\n" +
                            "    \"status\": 422,\n" +
                            "    \"detail\": \"Email já cadastrado.\",\n" +
                            "    \"instance\": \"/api/v1/app/noauth/users\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public AppUserResponse create (
            @RequestBody AppUserCreateRequest request) {
        return userService.create(request);
    }

    @Operation(summary = "Serviço que recupera senha do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao enviar email. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/noauth/users/{email}/forget\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Email não está cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Email não cadastrado.\",\n" +
                            "    \"instance\": \"/api/v1/app/noauth/users/{email}/forget\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/forget")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void forget (
            @RequestBody AppUserForgetRequest request) {
        userService.forget(request);
    }
}
