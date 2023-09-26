package br.com.petshop.system.employee.controller;

import br.com.petshop.system.employee.model.dto.request.EmployeeCreateRequest;
import br.com.petshop.system.employee.model.dto.request.EmployeeUpdateRequest;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import br.com.petshop.system.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/system/employees")
@Tag(name = "Employee Services")
public class EmployeeController {

    @Autowired private EmployeeService employeeService;

    //SOMENTE 'ADMIN', 'OWNER', 'MANAGER'
    @Operation(summary = "Serviço de inclusão da funcionário no sistema.",
    description = "Acesso: admin, owner e manager")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Funcionário já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Funcionário já cadastrado no sistema.\",\n" +
                            "\"instance\": \"/api/v1/system/employees\"\n" +
                            "}")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar funcionário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/employees\"\n" +
                            "}")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public EmployeeResponse create(Principal authentication,
                                   @RequestBody EmployeeCreateRequest request) {
        return employeeService.create(authentication, request);
    }

    //SOMENTE 'ADMIN', 'OWNER', 'MANAGER'
    @Operation(summary = "Serviço de atualização de qualquer funcionário no sistema.",
            description = "Acesso: admin, owner e manager")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro do funcionário não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Funcionário não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados do funcionário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public EmployeeResponse updateById(
            Principal authentication,
            @PathVariable("employeeId") String employeeId,
            @RequestBody EmployeeUpdateRequest request) {
        return employeeService.updateById(authentication, employeeId, request);
    }

    //ACESSO: TODOS OS USERS
    @Operation(summary = "Serviço de atualização dos dados do funcionário logado no sistema.",
            description = "Acesso: todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados do funcionário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse update(
            Principal authentication,
            @RequestBody EmployeeUpdateRequest request) {
        return employeeService.update(authentication, request);
    }

    //SOMENTE SYSTEM_ADMIN

    @Operation(summary = "Serviço de recuperação das informações do funcionário no sistema.",
            description = "Acesso: admin, owner e manager")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro do funcionário não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do funcionário não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do funcionário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public EmployeeResponse getById(
            Principal authentication,
            @PathVariable("employeeId") String employeeId) {
        return employeeService.getById(authentication, employeeId);
    }

    //ACESSO: TODOS OS USUARIOS
    @Operation(summary = "Serviço de recuperação das informações do funcionário no sistema.",
            description = "Acesso: todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro do funcionário não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do funcionário não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/employees\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do funcionário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/employees\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse get(
            Principal authentication) {
        return employeeService.get(authentication);
    }

    //SOMENTE 'ADMIN', 'OWNER', 'MANAGER'
    @Operation(summary = "Serviço de exclusão do cadastro do funcionário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadasteo do funcionário não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do funcionário não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do funcionário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/employees/{employeeId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public void deactivate(
            @PathVariable("employeeId") String employeeId) {
        employeeService.deactivate(employeeId);
    }
}
