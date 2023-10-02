package br.com.petshop.app.address.controller;

import br.com.petshop.app.address.model.dto.request.AppAddressCreateRequest;
import br.com.petshop.app.address.model.dto.request.AppAddressUpdateRequest;
import br.com.petshop.app.address.model.dto.response.AppAddressResponse;
import br.com.petshop.app.address.service.AppAddressService;
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
import java.util.Set;

@RestController
@RequestMapping("/api/v1/app/users/addresses")
@Tag(name = "User Address Services")
public class AppAddressController {
    @Autowired private AppAddressService addressService;

    @Operation(summary = "Serviço de inclusão de endereço no cadastro do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar novo endereço. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/users/addresses\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public AppAddressResponse create(
            Principal authentication,
            @RequestBody AppAddressCreateRequest request) {
        return addressService.create(authentication, request);
    }

    @Operation(summary = "Serviço de atualização de endereço no cadastro do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao excluir endereço do cadastro. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/users/addresses/{idAddress}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public AppAddressResponse update(
            @PathVariable ("addressId") String addressId,
            @RequestBody AppAddressUpdateRequest request) {
        return addressService.update(addressId, request);
    }

    @Operation(summary = "Serviço para setar o endereço principal do usuáro no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao setar endereço principal. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/users/addresses/{idAddress}\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PatchMapping("/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public void setPrincipal(
            Principal authentication,
            @PathVariable ("addressId") String addressId) {
        addressService.setPrincipal(authentication, addressId);
    }

    @Operation(summary = "Serviço de recuperação de todos os endereços do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do endereço. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/users/addresses\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Set<AppAddressResponse> get(
            Principal authentication) {
        return addressService.get(authentication);
    }

    @Operation(summary = "Serviço de exclusão de endereço no cadastro do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao excluir endereço do cadastro. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/users/addresses/{idAddress}\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            Principal authentication,
            @PathVariable ("addressId") String addressId) {
        addressService.delete(authentication, addressId);
    }
}
