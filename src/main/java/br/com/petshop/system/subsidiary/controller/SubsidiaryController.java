package br.com.petshop.system.subsidiary.controller;

import br.com.petshop.system.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.system.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.system.company.model.dto.response.CompanyResponse;
import br.com.petshop.system.subsidiary.model.dto.request.SubsidiaryCreateRequest;
import br.com.petshop.system.subsidiary.model.dto.request.SubsidiaryUpdateRequest;
import br.com.petshop.system.subsidiary.model.dto.response.SubsidiaryResponse;
import br.com.petshop.system.subsidiary.service.SubsidiaryService;
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
import java.util.Set;

@RestController
@RequestMapping("/api/v1/system/subsidiaries")
@Tag(name = "Subsidiary Services")
public class SubsidiaryController {

    @Autowired private SubsidiaryService subsidiaryService;

    @Operation(summary = "Serviço de inclusão do estabelecimento no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "422",
                    description = "Estabelecimento já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Empresa já cadastrada no sistema.\",\n" +
                            "\"instance\": \"/api/v1/system/subsidiaries\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar estabelecimento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/subsidiaries\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public SubsidiaryResponse create(Principal authentication,
                                  @RequestBody SubsidiaryCreateRequest request) {
        return subsidiaryService.create(authentication, request);
    }

    //SOMENTE ADMIN
    @Operation(summary = "Serviço de atualização do estabelecimento no sistema pelo id.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro do estabelecimento não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do estabelecimento não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/subsidiaries/{subsidiaryId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados do estabelecimento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/subsidiaries/{subsidiaryId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{subsidiaryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public SubsidiaryResponse updateById(
            @PathVariable("subsidiaryId") String subsidiaryId,
            @RequestBody SubsidiaryUpdateRequest request) {
        return subsidiaryService.updateById(subsidiaryId, request);
    }

    //SOMENTE OWNER
    @Operation(summary = "Serviço de atualização dos dados do estabelecimento do login.",
            description = "Acesso: 'OWNER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados do estabelecimento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/subsidiaries\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('OWNER')")
    public SubsidiaryResponse update(
            Principal authentication,
            @RequestBody SubsidiaryUpdateRequest request) {
        return subsidiaryService.update(authentication, request);
    }

    //SOMENTE ADMIN
    @Operation(summary = "Serviço de recuperação das informações do estabelecimento no sistema pelo id.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro do estabelecimento não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do estabelecimento não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/subsidiaries/{subsidiaryId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do estabelecimento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/subsidiaries/{subsidiaryId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{subsidiaryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public SubsidiaryResponse getByCompanyId(
            Principal authentication,
            @PathVariable("subsidiaryId") String subsidiaryId) {
        return subsidiaryService.getByCompanyId(authentication, subsidiaryId);
    }

    //SOMENTE OWNER
    @Operation(summary = "Serviço de recuperação das informações do estabelecimento do login.",
            description = "Acesso: 'OWNER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do estabelecimento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/subsidiaries\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('OWNER')")
    public SubsidiaryResponse get(
            Principal authentication) {
        return subsidiaryService.get(authentication);
    }

    //SOMENTE ADMIN
    @Operation(summary = "Serviço de desativação do estabelecimento no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadasteo do estabelecimento não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro da empresa não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/subsidiaries/{subsidiaryId}/deactivate\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao desativar estabelecimento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/subsidiaries/{subsidiaryId}/deactivate\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/{subsidiaryId}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deactivate(
            @PathVariable("subsidiaryId") String subsidiaryId) {
        subsidiaryService.deactivate(subsidiaryId);
    }

    //SOMENTE ADMIN
    @Operation(summary = "Serviço de ativação do estabelecimmento no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadasteo do estabelecimento não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do estabelecimento não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/subsidiaries/{subsidiaryId}/activate\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao ativar estabelecimento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/subsidiaries/{subsidiaryId}/activate\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/{subsidiaryId}/activate")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void activate(
            @PathVariable("subsidiaryId") String subsidiaryId) {
        subsidiaryService.activate(subsidiaryId);
    }

    //SOMENTE ADMIN
    @Operation(summary = "Serviço de exclusão do estabelecimento do sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadasteo do estabelecimento não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do estabelecimento não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/subsidiaries/{subsidiaryId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao excluir estabelecimento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/subsidiaries/{subsidiaryId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @DeleteMapping("/{subsidiaryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(
            @PathVariable("subsidiaryId") String subsidiaryId) {
        subsidiaryService.delete(subsidiaryId);
    }
}
