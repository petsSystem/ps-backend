package br.com.petshop.customer.controller;

import br.com.petshop.customer.model.dto.request.sys.CustomerSysCreateRequest;
import br.com.petshop.customer.model.dto.request.sys.CustomerSysUpdateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.dto.response.CustomerTableResponse;
import br.com.petshop.customer.service.sys.CustomerSysBusinessService;
import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
 * Classe responsável pelos endpoints de clientes do sistema web.
 */
@RestController
@RequestMapping("/api/v1/pet/sys/customers")
@Tag(name = "Serviços para Clientes")
public class CustomerSYSController {
    @Autowired private CustomerSysBusinessService businessService;

    @Operation(summary = "Serviço de inclusão de cliente no sistema pela loja.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar cliente. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/sys/customers\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cliente não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/sys/customers\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Cliente já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Cliente já cadastrado.\",\n" +
                            "\"instance\": \"/api/v1/pet/sys/customers\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(
            @RequestBody @Valid CustomerSysCreateRequest request) {
        return businessService.create(request);
    }

    @Operation(summary = "Serviço de alteração dos dados do usuário.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar cliente. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/sys/customers/{customerId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cliente não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/sys/customers/{customerId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse update (
            Principal authentication,
            @PathVariable("customerId") UUID customerId,
            @RequestBody CustomerSysUpdateRequest request) {
        return businessService.update(authentication, customerId, request);
    }

    @Operation(summary = "Serviço de associação de petshop com cliente.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao associar petshop ao cliente. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/customers/{customerId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cliente não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/sys/customers/{customerId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/{customerId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse associateCompanyId (
            Principal authentication,
            @PathVariable("customerId") UUID customerId,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"add\",\n" +
                    "        \"path\": \"/companyIds/0\",\n" +
                    "        \"value\": \"${companyId}\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return businessService.associateCompanyId(authentication, customerId, patch);
    }

    @Operation(summary = "Serviço para recuperar dados do cadastro do usuário no APP.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao retornar dados dos clientes. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/sys/customers\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<CustomerTableResponse> get (
            Principal authentication,
            @RequestParam("companyId") UUID companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return businessService.get(authentication, companyId, pageable);
    }

    @Operation(summary = "Serviço para recuperar dados do cadastro do usuário no APP.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao retornar dados do usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/sys/customers\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cliente não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/sys/customers/{customerId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse getById (
            Principal authentication,
            @PathVariable("customerId") UUID id) {
        return businessService.getById(authentication, id);
    }
}