package br.com.petshop.user.controller;

import br.com.petshop.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.user.model.dto.request.SysUserPasswordRequest;
import br.com.petshop.user.model.dto.request.SysUserUpdateProfileRequest;
import br.com.petshop.user.model.dto.request.SysUserUpdateRequest;
import br.com.petshop.user.model.dto.response.SysUserMeResponse;
import br.com.petshop.user.model.dto.response.SysUserProfileResponse;
import br.com.petshop.user.model.dto.response.SysUserResponse;
import br.com.petshop.user.model.dto.response.SysUserTableResponse;
import br.com.petshop.user.service.SysUserBusinessService;
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
import java.util.UUID;

/**
 * Classe responsável pelos endpoints de usuários do sistema web.
 */
@RestController
@RequestMapping("/api/v1/pet/users")
@Tag(name = "Users Services")
public class SysUserController {

    @Autowired private SysUserBusinessService businessService;

    @Operation(summary = "Serviço de inclusão de usuário no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/users\"\n" +
                            "}")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Loja inativa.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Bad Request\",\n" +
                            "    \"status\": 400,\n" +
                            "    \"detail\": \"Loja inativa.\",\n" +
                            "    \"instance\": \"/api/v1/pet/users\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Forbidden\",\n" +
                            "    \"status\": 403,\n" +
                            "    \"detail\": \"Acesso negado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/users\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Loja não encontrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Loja não encontrada.\",\n" +
                            "    \"instance\": \"/api/v1/pet/users\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Usuário já cadastrado no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Usuário já cadastrado no sistema.\",\n" +
                            "\"instance\": \"/api/v1/pet/users\"\n" +
                            "}")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public SysUserResponse create(
            Principal authentication,
            @RequestBody SysUserCreateRequest request) {
        return businessService.create(authentication, request);
    }

    @Operation(summary = "Serviço de atualização de usuário no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse (
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados do usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/users/{userId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Forbidden\",\n" +
                            "    \"status\": 403,\n" +
                            "    \"detail\": \"Acesso negado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/users/{userId}\"\n" +
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
                            "    \"instance\": \"/api/v1/pet/users/{userId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public SysUserResponse updateById(
            Principal authentication,
            @PathVariable("userId") UUID userId,
            @RequestBody SysUserUpdateRequest request) {
        return businessService.updateById(authentication, userId, request);
    }

    @Operation(summary = "Serviço de ativação/desativação de usuário no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao ativar/desativar usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/users/{userId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Forbidden\",\n" +
                            "    \"status\": 403,\n" +
                            "    \"detail\": \"Acesso negado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/users\"\n" +
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
                            "    \"instance\": \"/api/v1/pet/users/{userId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/{userId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public SysUserResponse activate(
            Principal authentication,
            @PathVariable("userId") UUID userId,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"/active\",\n" +
                    "        \"value\": \"true\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return businessService.activate(authentication, userId, patch);
    }

    @Operation(summary = "Serviço de recuperação das informações de usuários do sistema, de acordo com o companyId informado.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados dos usuários. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/users\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Forbidden\",\n" +
                            "    \"status\": 403,\n" +
                            "    \"detail\": \"Acesso negado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/users\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<SysUserTableResponse> get(
            Principal authentication,
            @RequestParam(value = "companyId", required = true) UUID companyId,
            @RequestParam(value = "productId", required = false) UUID productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return businessService.get(authentication, companyId, productId, pageable);
    }

    @Operation(summary = "Serviço de recuperação das informações do usuário no sistema através do id.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/users/{userId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Forbidden\",\n" +
                            "    \"status\": 403,\n" +
                            "    \"detail\": \"Acesso negado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/users/{userId}\"\n" +
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
                            "    \"instance\": \"/api/v1/sys/users/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public SysUserResponse getById(
            Principal authentication,
            @PathVariable("userId") UUID userId) {
        return businessService.getById(authentication, userId);
    }

    @Operation(summary = "Serviço de recuperação dos dados do usuário logado no sistema.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/users/me\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public SysUserMeResponse me(
            Principal authentication) {
        return businessService.getMeInfo(authentication);
    }

    @Operation(summary = "Serviço de recuperação dos dados do usuário logado no sistema.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/users/profile\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public SysUserProfileResponse getProfile(
            Principal authentication) {
        return businessService.getProfile(authentication);
    }

    @Operation(summary = "Serviço de atualização do perfil do usuário logado.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse (
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados do usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/users/profile\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public SysUserProfileResponse updateProfile(
            Principal authentication,
            @RequestBody SysUserUpdateProfileRequest request) {
        return businessService.updateProfile(authentication, request);
    }

    @Operation(summary = "Serviço de alteração de senha no sistema.",
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
                            "\"instance\": \"/api/v1/pet/users/password\"\n" +
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
                            "    \"instance\": \"/api/v1/pet/users/password\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public SysUserResponse changePassword (
            Principal authentication,
            @RequestBody SysUserPasswordRequest request) {
        return businessService.changePassword(authentication, request);
    }

    @Operation(summary = "Serviço de marcação de último Petshop acessado.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao marcar última loja/petshop acessada. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/users/current\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/current", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    public SysUserMeResponse currentCompany(
            Principal authentication,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"/currentCompany\",\n" +
                    "        \"value\": \"companyId\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return businessService.updateCurrentCompany(authentication, patch);
    }
}
