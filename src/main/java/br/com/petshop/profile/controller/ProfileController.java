package br.com.petshop.profile.controller;

import br.com.petshop.profile.model.dto.request.ProfileCreateRequest;
import br.com.petshop.profile.model.dto.request.ProfileUpdateRequest;
import br.com.petshop.profile.model.dto.response.ProfileLabelResponse;
import br.com.petshop.profile.model.dto.response.ProfileResponse;
import br.com.petshop.profile.service.ProfileBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.util.UUID;

/**
 * Classe responsável pelos endpoints de perfis do sistema.
 */
@RestController
@RequestMapping("/api/v1/pet/profiles")
@Tag(name = "Profile Services")
public class ProfileController {

    @Autowired private ProfileBusinessService business;

    @Operation(summary = "Serviço de criação de perfil no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar perfil. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/profiles\"\n" +
                            "}")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Perfil já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Perfil já cadastrado.\",\n" +
                            "\"instance\": \"/api/v1/pet/profiles\"\n" +
                            "}")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProfileResponse create(
            Principal authentication,
            @RequestBody ProfileCreateRequest request) {
        return business.create(authentication, request);
    }

    @Operation(summary = "Serviço de atualização de perfil no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar perfil. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/profiles/{profileId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Perfil não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Perfil não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/profiles/{profileId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProfileResponse updateById(
            Principal authentication,
            @PathVariable("profileId") UUID profileId,
            @RequestBody ProfileUpdateRequest request) {
        return business.updateById(authentication, profileId, request);
    }

    @Operation(summary = "Serviço de recuperação de todos os perfis cadastrados no sistema.",
            description = "Acesso: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do perfil. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/profiles\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<ProfileResponse> get(
            Principal authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return business.get(authentication, pageable);
    }

    @Operation(summary = "Serviço de recuperação das informações do perfil pelo id.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar perfil. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/profiles/{profileId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Perfil não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Perfil não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/profiles/{profileId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProfileResponse getById (
            Principal authentication,
            @PathVariable("profileId") UUID profileId) {
        return business.getById(authentication, profileId);
    }

    @Operation(summary = "Serviço de recuperação dos labels dos perfis do sistema.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do perfil. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/profiles/labels\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileLabelResponse> getLabels(
            Principal authentication) {

        return business.getLabels(authentication);
    }
}
