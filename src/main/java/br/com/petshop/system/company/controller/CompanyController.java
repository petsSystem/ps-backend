package br.com.petshop.system.company.controller;

import br.com.petshop.system.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.system.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.system.company.model.dto.response.CompanyResponse;
import br.com.petshop.system.company.service.CompanyValidateService;
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
@RequestMapping("/api/v1/sys/companies")
@Tag(name = "SYS - Companies Services")
public class CompanyController {

    @Autowired private CompanyValidateService validateService;

    //ACESSO: ADMIN e OWNER
    @Operation(summary = "Serviço de inclusão de loja no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar loja. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/companies\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Loja já cadastrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Loja já cadastrada no sistema.\",\n" +
                            "\"instance\": \"/api/v1/sys/companies\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public CompanyResponse create(
            Principal authentication,
            @RequestBody CompanyCreateRequest request) {
        return validateService.create(authentication, request);
    }
    //ACESSO ADMIN
    @Operation(summary = "Serviço de ativação/desativação de loja no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar loja. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/companies/{companyId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Loja não encontrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro da loja não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/companies/{companyId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/{companyId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public CompanyResponse activate (
            Principal authentication,
            @PathVariable("companyId") UUID companyId,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"/active\",\n" +
                    "        \"value\": \"true\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return validateService.activate(authentication, companyId, patch);
    }

    //ACESSO: ADMIN, OWNER, MANAGER
    @Operation(summary = "Serviço de atualização da empresa no sistema pelo id.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados da empresa. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/companies/{companiesId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Loja inativa.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Bad Request\",\n" +
                            "    \"status\": 400,\n" +
                            "    \"detail\": \"Loja inativa.\",\n" +
                            "    \"instance\": \"/api/v1/sys/companies/{companyId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro da empresa não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro da empresa não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/companies/{companiesId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyResponse updateById(
            Principal authentication,
            @PathVariable("companyId") UUID companyId,
            @RequestBody CompanyUpdateRequest request) {
        return validateService.updateById(authentication, companyId, request);
    }

    //ACESSO: ALL
    @Operation(summary = "Serviço de recuperação das informações da(s) loja(s) do usuário autenticado.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados da(s) loja(s). Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/companies\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<CompanyResponse> get (
            Principal authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        return validateService.get(authentication, paging);
    }

    //ACESSO: ALL
    @Operation(summary = "Serviço de recuperação das informações da(s) loja(s) pelo id.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados da(s) loja(s). Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/companies/{companiesId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Forbidden\",\n" +
                            "\"status\": 403,\n" +
                            "\"detail\": \"Acesso negado.\",\n" +
                            "\"instance\": \"/api/v1/sys/companies/{companiesId}\"\n" +
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
                            "    \"instance\": \"/api/v1/sys/companies/{companiesId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyResponse getById (
            Principal authentication,
            @PathVariable("companyId") UUID companyId) {
        return validateService.getById(authentication, companyId);
    }
}
