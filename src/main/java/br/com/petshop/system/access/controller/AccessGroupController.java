package br.com.petshop.system.access.controller;

import br.com.petshop.system.access.model.dto.request.AccessGroupCreateRequest;
import br.com.petshop.system.access.model.dto.request.AccessGroupUpdateRequest;
import br.com.petshop.system.access.model.dto.response.AccessGroupResponse;
import br.com.petshop.system.access.service.AccessGroupService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/system/access")
@Tag(name = "System Profile Services")
public class AccessGroupController {
    @Autowired private AccessGroupService accessGroupService;

    @Operation(summary = "Serviço que cria grupo de acesso no Sistema PetShop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "422",
                    description = "Grupo de acesso já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Unprocessable Entity\",\n" +
                            "    \"status\": 422,\n" +
                            "    \"detail\": \"Grupo de acesso já cadastrado.\",\n" +
                            "    \"instance\": \"/api/v1/system/users\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar grupo de acesso. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/users\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public AccessGroupResponse create (
            @RequestBody AccessGroupCreateRequest request) {
        return accessGroupService.create(request);
    }

    @Operation(summary = "Serviço de alteração dos dados de grupo de acesso do Sistema PetShop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados do grupo de acesso. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/users\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PutMapping("/{accessGroupId}")
    @ResponseStatus(HttpStatus.OK)
    public AccessGroupResponse update (
            Principal authentication,
            @PathVariable ("accessGroupId") String accessGroupId,
            @RequestBody AccessGroupUpdateRequest request) {
        return accessGroupService.update(authentication, accessGroupId, request);
    }


    @Operation(summary = "Serviço para recuperar dados de grupo de acesso do Sistema PetShop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao retornar dados do usuário. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/users\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<AccessGroupResponse> getAll (
            Principal authentication) {
        return accessGroupService.findAll(authentication);
    }

    @Operation(summary = "Serviço para excluir um grupo de acesso no Sistema PetShop.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao excluir grupo de acesso. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/system/users/{email}/deactivate\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @DeleteMapping("/{accessGroupId}")
    @ResponseStatus(HttpStatus.OK)
    public void deactivate (
            @PathVariable ("accessGroupId") String accessGroupId) {
        accessGroupService.delete(accessGroupId);
    }
}
