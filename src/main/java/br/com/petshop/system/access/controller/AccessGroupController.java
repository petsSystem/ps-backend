package br.com.petshop.system.access.controller;

import br.com.petshop.system.access.model.dto.request.AccessGroupCreateRequest;
import br.com.petshop.system.access.model.dto.response.AccessGroupResponse;
import br.com.petshop.system.access.service.AccessGroupValidateService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sys/accesses")
@Tag(name = "System Profile Services")
public class AccessGroupController {

    @Autowired private AccessGroupValidateService accessGroupValidateService;

    //ACESSO: 'ADMIN'
    @Operation(summary = "Serviço de criação de grupo de acesso no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar grupo de acesso. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/accesses\"\n" +
                            "}")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Grupo de acesso já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Grupo de acesso já cadastrado no sistema.\",\n" +
                            "\"instance\": \"/api/v1/sys/accesses\"\n" +
                            "}")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public AccessGroupResponse create(
            Principal authentication,
            @RequestBody AccessGroupCreateRequest request) {
        return accessGroupValidateService.create(authentication, request);
    }

    @Operation(summary = "Serviço de atualização parcial de grupo de acesso no sistema.",
            description = "Acesso: 'ALL'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar parcialmente os dados do grupo de acesso. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/accesses/{accessGroupId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro de funcionário não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de usuário não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/accesses/{accessGroupId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/{accessGroupId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AccessGroupResponse> partialUpdate(
            Principal authentication,
            @PathVariable("accessGroupId") UUID accessGroupId,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"/active\",\n" +
                    "        \"value\": \"true\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return accessGroupValidateService.partialUpdate(authentication, accessGroupId, patch);
    }

    //ACESSO: ADMIN
    @Operation(summary = "Serviço de recuperação de grupos de acesso do sistema.",
            description = "Acesso: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar grupos de acesso. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/accesses\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<AccessGroupResponse> getAll(
            Principal authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return accessGroupValidateService.getAll(authentication, pageable);
    }

    @Operation(summary = "Serviço de recuperação dos grupos de acesso do sistema do usuário logado.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar grupos de acesso. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/accesses\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<AccessGroupResponse> get(
            Principal authentication) {

        return accessGroupValidateService.get(authentication);
    }

    //ACESSO: ALL
    @Operation(summary = "Serviço de recuperação das informações do grupo de acesso através do id.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do grupo de acesso. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/accesses/{accessGroupId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro de grupo de acesso não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de grupo de acesso não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/accesses/{accessGroupId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{accessGroupId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public AccessGroupResponse getById(
            Principal authentication,
            @PathVariable("accessGroupId") UUID accessGroupId) {
        return accessGroupValidateService.getById(authentication, accessGroupId);
    }

    //ACESSO: 'ADMIN'
    @Operation(summary = "Serviço de exclusão de grupo de acesso do sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro do grupo de acesso não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro do grupo de acesso não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/accesses\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao excluir dados do grupo de acesso do sistema. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/accesses\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @DeleteMapping("/{accessGroupId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete (
            Principal authentication,
            @PathVariable("accessGroupId") UUID accessGroupId) {
        accessGroupValidateService.delete(authentication, accessGroupId);
    }
}
