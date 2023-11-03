package br.com.petshop.system.user.controller;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import br.com.petshop.system.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.system.user.model.dto.request.SysUserFilterRequest;
import br.com.petshop.system.user.model.dto.request.SysUserForgetRequest;
import br.com.petshop.system.user.model.dto.response.SysUserResponse;
import br.com.petshop.system.user.service.SysUserService;
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
@RequestMapping("/api/v1/sys/users")
@Tag(name = "SYS - Users Services")
public class SysUserController {
    @Autowired private SysUserService systemUserService;

    //ACESSO: 'ADMIN', 'OWNER', 'MANAGER'
    @Operation(summary = "Serviço de criação de usuário no sistema.",
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
                    responseCode = "403",
                    description = "Acesso proibido.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Forbidden\",\n" +
                            "\"status\": 403,\n" +
                            "\"detail\": \"Acesso proibido.\",\n" +
                            "\"instance\": \"/api/v1/system/users\"\n" +
                            "}")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Funcionário não existe.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Funcionário não existe.\",\n" +
                            "    \"instance\": \"/api/v1/system/users\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Usuário já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Usuário já cadastrado no sistema.\",\n" +
                            "\"instance\": \"/api/v1/system/users\"\n" +
                            "}")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public SysUserResponse create(Principal authentication,
                                   @RequestBody SysUserCreateRequest request) {
        return systemUserService.create(authentication, request);
    }

    @Operation(summary = "Serviço que envia nova senha por email no Sistema Petshop.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao enviar email com reset de senha. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys//users/forget\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Usuário não cadastrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/users/forget\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping("/forget")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void forget (
            @RequestBody SysUserForgetRequest request) {
        systemUserService.forget(request);
    }

    @Operation(summary = "Serviço de atualização parcial de usuário no sistema.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar parcialmente os dados do usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/users/{userId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro de funcionário não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de usuário não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/users/{userId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/{userId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    public List<SysUserResponse> partialUpdate(
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
        return systemUserService.partialUpdate(authentication, userId, patch);
    }

    //ACESSO: ALL (COM FILTROS)
    @Operation(summary = "Serviço de recuperação das informações do usuário no sistema.",
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
                            "\"instance\": \"/api/v1/sys/users\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<SysUserResponse> get(
            Principal authentication,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UUID employeeId,
            @RequestParam(required = false) UUID accessGroupId,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        SysUserFilterRequest filter = new SysUserFilterRequest(email, employeeId, accessGroupId, role, active, null);
        return systemUserService.get(authentication, pageable, filter);
    }

    //ACESSO: ALL
    @Operation(summary = "Serviço de recuperação das informações do usuário através do id.",
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
                            "\"instance\": \"/api/v1/sys/users/{userId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário não pertence a mesma empresa/loja do usuário logado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Forbidden\",\n" +
                            "    \"status\": 403,\n" +
                            "    \"detail\": \"Acesso proibido.\",\n" +
                            "    \"instance\": \"/api/v1/sys/users/{userId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro de usuário não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de usuário não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/users/{userId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public SysUserResponse getById(
            Principal authentication,
            @PathVariable("userId") UUID userId) {
        return systemUserService.getById(authentication, userId);
    }

    //ACESSO: 'ADMIN'
    @Operation(summary = "Serviço de exclusão do usuário do sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro do usuário não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do usuário não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/users\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao excluir dados do usuário do sistema. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/users\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete (
            @PathVariable("userId") UUID userId) {
        systemUserService.delete(userId);
    }
}
