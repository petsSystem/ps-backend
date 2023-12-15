package br.com.petshop.system.user.controller;

import br.com.petshop.system.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.system.user.model.dto.request.SysUserUpdateRequest;
import br.com.petshop.system.user.model.dto.response.SysUserMeResponse;
import br.com.petshop.system.user.model.dto.response.SysUserProfileResponse;
import br.com.petshop.system.user.model.dto.response.SysUserResponse;
import br.com.petshop.system.user.model.dto.response.SysUserTableResponse;
import br.com.petshop.system.user.service.SysUserValidateService;
import br.com.petshop.system.user.model.dto.request.SysUserForgetRequest;
import br.com.petshop.system.user.model.dto.request.SysUserPasswordRequest;
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

@RestController
@RequestMapping("/api/v1/sys/users")
@Tag(name = "SYS - Users Services")
public class SysUserController {

    @Autowired private SysUserValidateService validateService;

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
                            "\"instance\": \"/api/v1/system/users\"\n" +
                            "}")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Loja inativa.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Bad Request\",\n" +
                            "    \"status\": 400,\n" +
                            "    \"detail\": \"Loja inativa.\",\n" +
                            "    \"instance\": \"/api/v1/sys/users\"\n" +
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
                            "    \"instance\": \"/api/v1/system/users\"\n" +
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
                            "\"instance\": \"/api/v1/system/users\"\n" +
                            "}")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public SysUserResponse create(
            Principal authentication,
            @RequestBody SysUserCreateRequest request) {
        return validateService.create(authentication, request);
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
                            "\"instance\": \"/api/v1/sys/users/password\"\n" +
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
                            "    \"instance\": \"/api/v1/sys/users/{userId}/password\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public SysUserResponse changePassword (
            Principal authentication,
            @RequestBody SysUserPasswordRequest request) {
        return validateService.changePassword(authentication, request);
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
                            "\"instance\": \"/api/v1/sys/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public SysUserProfileResponse getProfile(
            Principal authentication) {
        return validateService.getProfile(authentication);
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
                            "\"instance\": \"/api/v1/sys/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public SysUserMeResponse me(
            Principal authentication) {
        return validateService.getMeInfo(authentication);
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
                            "\"instance\": \"/api/v1/sys/users/{userId}\"\n" +
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
                            "    \"instance\": \"/api/v1/sys/users/{userId}\"\n" +
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
        return validateService.activate(authentication, userId, patch);
    }

    @Operation(summary = "Serviço de atualização de qualquer usuário no sistema.",
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
                            "\"instance\": \"/api/v1/sys/users/{userId}\"\n" +
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
                            "    \"instance\": \"/api/v1/sys/users/{userId}\"\n" +
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
                            "    \"instance\": \"/api/v1/sys/users/{userId}\"\n" +
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
        return validateService.updateById(authentication, userId, request);
    }

    @Operation(summary = "Serviço de recuperação das informações de funcionários do sistema.",
            description = "Acesso: ADMIN, OWNER, MANAGER")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados dos funcionários. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/employees\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public Page<SysUserTableResponse> get(
            Principal authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return validateService.get(authentication, pageable);
    }

    @Operation(summary = "Serviço de recuperação das informações do funcionário no sistema através do id.",
            description = "Acesso: ADMIN, OWNER, MANAGER")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do funcionário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Funcionário não pertence a mesma empresa/loja do usuário logado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Forbidden\",\n" +
                            "    \"status\": 403,\n" +
                            "    \"detail\": \"Acesso proibido.\",\n" +
                            "    \"instance\": \"/api/v1/sys/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro de funcionário não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de funcionário não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public SysUserResponse getById(
            Principal authentication,
            @PathVariable("employeeId") UUID employeeId) {
        return validateService.getById(authentication, employeeId);
    }

}
