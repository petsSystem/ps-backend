package br.com.petshop.system.profile.controller;

import br.com.petshop.system.profile.model.dto.request.ProfileCreateRequest;
import br.com.petshop.system.profile.model.dto.response.LabelResponse;
import br.com.petshop.system.profile.model.dto.response.ProfileResponse;
import br.com.petshop.system.profile.service.ProfileValidateService;
import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sys/profiles")
@Tag(name = "SYS - Profile Services")
public class ProfileController {

    @Autowired private ProfileValidateService profileValidateService;

    //ACESSO: 'ADMIN'
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
                            "\"instance\": \"/api/v1/sys/profiles\"\n" +
                            "}")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Perfil já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Perfil já cadastrado no sistema.\",\n" +
                            "\"instance\": \"/api/v1/sys/profiles\"\n" +
                            "}")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProfileResponse create(
            Principal authentication,
            @RequestBody ProfileCreateRequest request) {
        return profileValidateService.create(authentication, request);
    }

    @Operation(summary = "Serviço de atualização parcial de perfil no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar parcialmente os dados do perfil. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/profiles/{profileId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro de perfil não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de perfil não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/profiles/{profileId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/{profileId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ProfileResponse> partialUpdate(
            Principal authentication,
            @PathVariable("profileId") UUID profileId,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"/active\",\n" +
                    "        \"value\": \"true\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return profileValidateService.partialUpdate(authentication, profileId, patch);
    }

    //ACESSO: ADMIN
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
                            "\"instance\": \"/api/v1/sys/profiles\"\n" +
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
        return profileValidateService.get(authentication, pageable);
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
                            "\"detail\": \"Erro ao recuperar labels dos perfis do sistema. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/profiles/labels\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    public List<LabelResponse> getLabels(
            Principal authentication) {

        return profileValidateService.getLabels(authentication);
    }

    //ACESSO: 'ADMIN'
    @Operation(summary = "Serviço de exclusão de perfil do sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro do perfil não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do perfil não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/profiles\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao excluir dados do perfil. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/profiles\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @DeleteMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete (
            Principal authentication,
            @PathVariable("profileId") UUID profileId) {
        profileValidateService.delete(authentication, profileId);
    }
}
