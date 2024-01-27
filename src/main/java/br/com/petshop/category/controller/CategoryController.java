package br.com.petshop.category.controller;

import br.com.petshop.category.model.dto.request.CategoryCreateRequest;
import br.com.petshop.category.service.CategoryFacadeService;
import br.com.petshop.category.model.dto.request.CategoryUpdateRequest;
import br.com.petshop.category.model.dto.response.CategoryResponse;
import br.com.petshop.category.model.dto.response.CategoryTableResponse;
import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pet/categories")
@Tag(name = "SYS - Category Services")
public class CategoryController {

    @Autowired private CategoryFacadeService facade;

    @Operation(summary = "Serviço de inclusão de categoria no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar categoria. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/categories\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Categoria já cadastrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Categoria já cadastrada.\",\n" +
                            "\"instance\": \"/api/v1/sys/categories\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public CategoryResponse create(
            Principal authentication,
            @RequestBody CategoryCreateRequest request) {
        return facade.create(authentication, request);
    }

    @Operation(summary = "Serviço de ativação/desativação de categoria no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao ativar/desativar categoria. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/categories/{categoryId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Categoria não encontrada.\",\n" +
                            "    \"instance\": \"/api/v1/sys/categories/{categoryId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/{categoryId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public CategoryResponse activate (
            Principal authentication,
            @PathVariable("categoryId") UUID categoryId,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"/active\",\n" +
                    "        \"value\": \"true\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return facade.activate(authentication, categoryId, patch);
    }

    @Operation(summary = "Serviço de atualização de categoria pelo id.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar categoria. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/categories/{categoryId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Categoria não encontrada.\",\n" +
                            "    \"instance\": \"/api/v1/sys/categories/{categoryId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public CategoryResponse updateById(
            Principal authentication,
            @PathVariable("categoryId") UUID categoryId,
            @RequestBody CategoryUpdateRequest request) {
        return facade.updateById(authentication, categoryId, request);
    }

    @Operation(summary = "Serviço de recuperação das informações da(s) categorias(s) de loja selecionada.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados da(s) categoria(s). Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/categories\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryTableResponse> get (
            Principal authentication,
            @RequestParam("companyId") UUID companyId) {
        return facade.getByCompanyId(authentication, companyId);
    }

    @Operation(summary = "Serviço de recuperação das informações da(s) categoria(s) pelo id.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados da categoria. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/categories/{categoryId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Categoria não encontrada.\",\n" +
                            "    \"instance\": \"/api/v1/sys/categories/{categoryId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getById (
            Principal authentication,
            @PathVariable("categoryId") UUID categoryId) {
        return facade.getById(authentication, categoryId);
    }
}
