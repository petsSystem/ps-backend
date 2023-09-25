package br.com.petshop.system.company.controller;

import br.com.petshop.system.company.service.CompanyService;
import br.com.petshop.system.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.system.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.system.company.model.dto.response.CompanyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/system/companies")
@Tag(name = "Company Services")
public class CompanyController {

    @Autowired private CompanyService companyService;


    //SOMENTE SYSTEM_ADMIN
    @Operation(summary = "Serviço de inclusão da empresa no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
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
    public CompanyResponse create(Principal authentication,
                                  @RequestBody CompanyCreateRequest request) {
        return companyService.create(authentication, request);
    }

    //SOMENTE SYSTEM_ADMIN E COMPANY_ADMIN
    @Operation(summary = "Serviço de atualização da empresa no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro da empresa não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Empresa não encontrada.\",\n" +
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
    public CompanyResponse update(
            Principal authentication,
            @PathVariable("companyId") String companyId,
            @RequestBody CompanyUpdateRequest request) {
        return companyService.update(authentication, companyId, request);
    }

    //SOMENTE SYSTEM_ADMIN

    @Operation(summary = "Serviço de recuperação das informações da empresa no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
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
    public CompanyResponse getByCompanyId(
            Principal authentication,
            @PathVariable("companyId") String companyId) {
        return companyService.getByCompanyId(authentication, companyId);
    }

    //SOMENTE SYSTEM_ADMIN E COMPANY_ADMIN
    @Operation(summary = "Serviço de recuperação das informações da empresa no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro da empresa não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro da empresa não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/companies\"\n" +
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
                            "\"instance\": \"/api/v1/system/companies\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public CompanyResponse get(
            Principal authentication) {
        return companyService.get(authentication);
    }

    //SOMENTE SYSTEM_ADMIN
    @Operation(summary = "Serviço de exclusão do cadastro da empresa no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
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
                            "\"detail\": \"Erro ao recuperar dados da empresa. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/companies/{companiesId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public void deactivate(
            @PathVariable("companyId") String companyId) {
        companyService.deactivate(companyId);
    }
}
