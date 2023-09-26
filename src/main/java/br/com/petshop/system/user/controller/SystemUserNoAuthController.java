package br.com.petshop.system.user.controller;

import br.com.petshop.system.user.service.SystemUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/noauth/users")
@RequiredArgsConstructor
@Tag(name = "System Users Services (NO AUTH)")
public class SystemUserNoAuthController {
    @Autowired private SystemUserService userService;

    @Operation(summary = "Serviço que recupera senha do usuário no Sistema Petshop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Email não está cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Email não cadastrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/noauth/users/{email}/forget\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao enviar email. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/noauth/users/{email}/forget\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @GetMapping("/{email}/forget")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void forget (
            @PathVariable("email") String email) {
        userService.forget(email);
    }
}
