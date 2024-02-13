package br.com.petshop.product.controller;

import br.com.petshop.product.model.dto.request.ProductCreateRequest;
import br.com.petshop.product.model.dto.request.ProductUpdateRequest;
import br.com.petshop.product.model.dto.response.ProductResponse;
import br.com.petshop.product.model.dto.response.ProductTableResponse;
import br.com.petshop.product.service.ProductBusinessService;
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
@RequestMapping("/api/v1/pet/products")
@Tag(name = "Product Services")
public class ProductController {

    @Autowired private ProductBusinessService businessService;

    @Operation(summary = "Serviço de inclusão de produto/serviço no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar produto/serviço. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/products\"\n" +
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
                            "\"instance\": \"/api/v1/pet/products\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Produto/serviço já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Produto/serviço já cadastrado.\",\n" +
                            "\"instance\": \"/api/v1/pet/products\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public ProductResponse create(
            Principal authentication,
            @RequestBody ProductCreateRequest request) {
        return businessService.create(authentication, request);
    }

    @Operation(summary = "Serviço de atualização de produto/serviço pelo id.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar produto/serviço. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/products/{productId}\"\n" +
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
                            "\"instance\": \"/api/v1/pet/products/{productId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto/serviço não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Produto/serviço não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/products/{productId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public ProductResponse updateById(
            Principal authentication,
            @PathVariable("productId") UUID productId,
            @RequestBody ProductUpdateRequest request) {
        return businessService.updateById(authentication, productId, request);
    }

    @Operation(summary = "Serviço de ativação/desativação de produto/serviço no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao ativar/desativar produto/serviço. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/products/{productId}\"\n" +
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
                            "\"instance\": \"/api/v1/pet/products/{productId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto/serviço não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Produto/serviço não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/products/{productId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/{productId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public ProductResponse activate (
            Principal authentication,
            @PathVariable("productId") UUID productId,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"/active\",\n" +
                    "        \"value\": \"true\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return businessService.activate(authentication, productId, patch);
    }

    @Operation(summary = "Serviço de recuperação das informações do(s) produto(s)/serviço(s) de categoria/loja selecionada.",
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
                            "\"instance\": \"/api/v1/pet/products\"\n" +
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
                            "\"instance\": \"/api/v1/pet/products\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductTableResponse> get (
            Principal authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "companyId", required = true) UUID companyId) {
        Pageable paging = PageRequest.of(page, size);
        return businessService.getAll(authentication, paging, companyId);
    }

    @Operation(summary = "Serviço de recuperação das informações do(s) produto(s)/serviço(s) pelo id.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do produto/serviço. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/products/{productId}\"\n" +
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
                            "\"instance\": \"/api/v1/pet/products/{productId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto/serviço não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Produto/serviço não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/products/{productId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getById (
            Principal authentication,
            @PathVariable("productId") UUID productId) {
        return businessService.getById(authentication, productId);
    }
}
