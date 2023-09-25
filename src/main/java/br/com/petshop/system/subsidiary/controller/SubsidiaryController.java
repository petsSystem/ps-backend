package br.com.petshop.system.subsidiary.controller;

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
import java.util.Set;

@RestController
@RequestMapping("/api/v1/system/subsidiary")
@Tag(name = "Subsidiary Services")
public class SubsidiaryController {

    @Autowired private SubsidiaryService subsidiaryService;

    //SOMENTE SYSTEM_ADMIN E COMPANY_ADMIN

    @Operation(summary = "Serviço de inclusão de estabelecimento no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Estabelecimento já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Estabelecimento já cadastrado.\",\n" +
                            "\"instance\": \"/api/v1/system/subsidiary\"\n" +
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
                            "\"instance\": \"/api/v1/system/subsidiary\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public SubsidiaryResponse create(Principal authentication,
                                     @RequestBody SubsidiaryCreateRequest request) {
        return subsidiaryService.create(authentication, request);
    }

    //SOMENTE SYSTEM_ADMIN E COMPANY_ADMIN E SUBSIDIARY_ADMIN

    @Operation(summary = "Serviço de atualização do estabelecimento no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro do estabelecimento não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Estabelecimento não encontrada.\",\n" +
                            "    \"instance\": \"/api/v1/system/subsidiary/{subsidiaryId}\"\n" +
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
                            "\"instance\": \"/api/v1/system/subsidiary/{subsidiaryId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public SubsidiaryResponse update(
            Principal authentication,
            @PathVariable("subsidiaryId") String subsidiaryId,
            @RequestBody SubsidiaryUpdateRequest request) {
        return subsidiaryService.update(authentication, subsidiaryId, request);
    }

    //SOMENTE SYSTEM_ADMIN

    @Operation(summary = "Serviço de recuperação das informações do estabelecimento no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro do estabelecimento não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do estabelecimento não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/subsidiary/{subsidiaryId}\"\n" +
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
                            "\"instance\": \"/api/v1/system/subsidiary/{subsidiaryId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{subsidiaryId}")
    @ResponseStatus(HttpStatus.OK)
    public SubsidiaryResponse getBySubsidiaryId(
            Principal authentication,
            @PathVariable("subsidiaryId") String subsidiaryId) {
        return subsidiaryService.getBySubsidiaryId(authentication, subsidiaryId);
    }

    //SOMENTE SYSTEM_ADMIN E COMPANY_ADMIN E SUBSIDIARY_ADMIN

    @Operation(summary = "Serviço de recuperação das informações do estabelecimento no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro do estabelecimento não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do estabelecimento não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/subsidiary/{subsidiaryId}\"\n" +
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
                            "\"instance\": \"/api/v1/system/subsidiary/{subsidiaryId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Set<SubsidiaryResponse> get(
            Principal authentication) {
        return subsidiaryService.get(authentication);
    }

    //SOMENTE SYSTEM_ADMIN E COMPANY_ADMIN

    @Operation(summary = "Serviço de exclusão do cadastro do estabelecimento no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadasteo da empresa não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do estabelecimento não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/subsidiary/{subsidiaryId}\"\n" +
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
                            "\"instance\": \"/api/v1/system/subsidiary/{subsidiaryId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @DeleteMapping("/{subsidiaryId}")
    @ResponseStatus(HttpStatus.OK)
    public void deactivate(
            @PathVariable("subsidiaryId") String subsidiaryId) {
        subsidiaryService.deactivate(subsidiaryId);
    }
}
