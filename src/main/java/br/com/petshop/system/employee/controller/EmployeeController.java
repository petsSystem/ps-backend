package br.com.petshop.system.employee.controller;

import br.com.petshop.system.employee.model.dto.request.EmployeeCreateRequest;
import br.com.petshop.system.employee.model.dto.request.EmployeeFilterRequest;
import br.com.petshop.system.employee.model.dto.request.EmployeeUpdateRequest;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import br.com.petshop.system.employee.service.EmployeeValidateService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sys/employees")
@Tag(name = "SYS - Employees Services")
public class EmployeeController {

    @Autowired private EmployeeValidateService validateService;

    //ACESSO: 'ADMIN', 'OWNER', 'MANAGER'
    @Operation(summary = "Serviço de inclusão da funcionário no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar funcionário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/employees\"\n" +
                            "}")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Empresa / Loja não existe.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Empresa / Loja não existe.\",\n" +
                            "    \"instance\": \"/api/v1/system/employees\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Funcionário já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Funcionário já cadastrado no sistema.\",\n" +
                            "\"instance\": \"/api/v1/system/employees\"\n" +
                            "}")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public EmployeeResponse create(
            Principal authentication,
            @RequestBody EmployeeCreateRequest request) {
        return validateService.create(authentication, request);
    }

    @Operation(summary = "Serviço de atualização parcial de funcionário no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar parcialmente os dados do funcionário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/employees/{employeeId}\"\n" +
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
    @PatchMapping(path = "/{employeeId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public EmployeeResponse activate(
            Principal authentication,
            @PathVariable("employeeId") UUID employeeId,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"/active\",\n" +
                    "        \"value\": \"true\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return validateService.activate(authentication, employeeId, patch);
    }

    //ACESSO: ALL (COM FILTROS)
    @Operation(summary = "Serviço de recuperação das informações do funcionário no sistema.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do funcionário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/employees\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Funcionário não pertence a empresa/loja informada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Forbidden\",\n" +
                            "    \"status\": 403,\n" +
                            "    \"detail\": \"Acesso proibido.\",\n" +
                            "    \"instance\": \"/api/v1/sys/employees\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Funcionário com mais de uma empresa associada. Necessário selecionar uma empresa/loja.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Funcionário com mais de uma empresa associada. Necessário selecionar uma empresa/loja.\",\n" +
                            "\"instance\": \"/api/v1/sys/employees\"\n" +
                            "}")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeResponse> get(
            Principal authentication,
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        EmployeeFilterRequest filter = new EmployeeFilterRequest(companyId, cpf, email, type, active);
        return validateService.get(authentication, pageable, filter);
    }

    //ACESSO: ALL
    @Operation(summary = "Serviço de recuperação das informações do funcionário no sistema através do id.",
            description = "Acesso: ALL")
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
    public EmployeeResponse getById(
            Principal authentication,
            @PathVariable("employeeId") UUID employeeId) {
        return validateService.getById(authentication, employeeId);
    }

    //ACESSO: ALL
    @Operation(summary = "Serviço de atualização de qualquer funcionário no sistema.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse (
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados do funcionário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário não tem permissão para alterar dados de funcionário.",
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
    @PutMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse updateById(
            Principal authentication,
            @PathVariable("employeeId") UUID employeeId,
            @RequestBody EmployeeUpdateRequest request) {
        return validateService.updateById(authentication, employeeId, request);
    }

    //ACESSO: 'ADMIN'
    @Operation(summary = "Serviço de exclusão do cadastro do funcionário no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadasteo do funcionário não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do funcionário não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao excluir dados do funcionário do sistema. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete (
            Principal authentication,
            @PathVariable("employeeId") UUID employeeId) {
        validateService.delete(authentication, employeeId);
    }
}
