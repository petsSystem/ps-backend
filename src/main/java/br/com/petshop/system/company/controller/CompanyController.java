package br.com.petshop.system.company.controller;

import br.com.petshop.system.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.system.company.service.CompanyService;
import br.com.petshop.system.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.system.company.model.dto.response.CompanyResponse;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/sys/companies")
@Tag(name = "Company Services")
public class CompanyController {

    @Autowired private CompanyService companyService;

    //SOMENTE ADMIN
    @Operation(summary = "Serviço de inclusão da empresa no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "422",
                    description = "Empresa já cadastrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Empresa já cadastrada no sistema.\",\n" +
                            "\"instance\": \"/api/v1/system/companies\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar empresa. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/companies\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public CompanyResponse create(Principal authentication,
                                  @RequestBody CompanyCreateRequest request) {
        return companyService.create(authentication, request);
    }

    //SOMENTE ADMIN
    @Operation(summary = "Serviço de atualização da empresa no sistema pelo id.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro da empresa não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro da empresa não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/companies/{companiesId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados da empresa. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/companies/{companiesId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public CompanyResponse updateById(
            @PathVariable("companyId") String companyId,
            @RequestBody CompanyUpdateRequest request) {
        return companyService.updateById(companyId, request);
    }

    //SOMENTE OWNER
    @Operation(summary = "Serviço de atualização dos dados da empresa do login.",
            description = "Acesso: 'OWNER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados da empresa. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/companies\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('OWNER')")
    public CompanyResponse update(
            Principal authentication,
            @RequestBody CompanyUpdateRequest request) {
        return companyService.update(authentication, request);
    }

    //SOMENTE ADMIN
    @Operation(summary = "Serviço de recuperação das informações da empresa no sistema pelo id.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro da empresa não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro da empresa não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/companies/{companiesId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados da empresa. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/companies/{companiesId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public CompanyResponse getByCompanyId(
            Principal authentication,
            @PathVariable("companyId") String companyId) {
        return companyService.getByCompanyId(authentication, companyId);
    }

    //SOMENTE OWNER
    @Operation(summary = "Serviço de recuperação das informações da empresa do login.",
            description = "Acesso: 'OWNER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados da empresa. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/companies\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('OWNER')")
    public CompanyResponse get(
            Principal authentication) {
        return companyService.get(authentication);
    }

    //SOMENTE ADMIN
    @Operation(summary = "Serviço de desativação do cadastro da empresa no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadasteo da empresa não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro da empresa não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/companies/{companiesId}/deactivate\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao desativar empresa. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/companies/{companiesId}/deactivate\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/{companyId}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deactivate(
            @PathVariable("companyId") String companyId) {
        companyService.deactivate(companyId);
    }

    //SOMENTE ADMIN
    @Operation(summary = "Serviço de ativação do cadastro da empresa no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadasteo da empresa não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro da empresa não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/companies/{companiesId}/activate\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao ativar empresa. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/companies/{companiesId}/activate\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/{companyId}/activate")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void activate(
            @PathVariable("companyId") String companyId) {
        companyService.activate(companyId);
    }

    //SOMENTE ADMIN
    @Operation(summary = "Serviço de exclusão do cadastro da empresa no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadasteo da empresa não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro da empresa não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/companies/{companiesId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao excluir empresa. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/companies/{companiesId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(
            @PathVariable("companyId") String companyId) {
        companyService.delete(companyId);
    }
}
