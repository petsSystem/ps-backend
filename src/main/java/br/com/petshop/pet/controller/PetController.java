package br.com.petshop.pet.controller;

import br.com.petshop.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.pet.model.dto.request.PetUpdateRequest;
import br.com.petshop.pet.model.dto.response.PetResponse;
import br.com.petshop.pet.service.PetBusinessService;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Classe responsável pelos endpoints de pets.
 */
@RestController
@RequestMapping("/api/v1/pet/pets")
@Tag(name = "Pets Services")
public class PetController {

    @Autowired private PetBusinessService businessService;

    @Operation(summary = "Serviço de recuperação de todas as racas de cães.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar lista de raças de cachorros. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/pets/dogs\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/dogs")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getDogsList(
            Principal authentication) {
        return businessService.getDogsList(authentication);
    }

    @Operation(summary = "Serviço de recuperação de todas as racas de gatos.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar lista de raças de gatos. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/pets/cats\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/cats")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getCatsList(
            Principal authentication) {
        return businessService.getCatsList(authentication);
    }

    @Operation(summary = "Serviço de cadastro de pet no sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar pet. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/pets\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public PetResponse create(Principal authentication,
                              @RequestBody @Valid PetCreateRequest request) {
        return businessService.create(authentication, request);
    }

    @Operation(summary = "Serviço de atualização do cadastro do Pet do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados do pet. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/pets/{petId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pet não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Pet não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/pets/{petId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{petId}")
    @ResponseStatus(HttpStatus.OK)
    public PetResponse update(
            Principal authentication,
            @PathVariable("petId") UUID petId,
            @RequestBody PetUpdateRequest request) {
        return businessService.update(authentication, petId, request);
    }

    @Operation(summary = "Serviço de recuperação das informações dos pets do cliente.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do(s) pet(s). Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/pets?customerId={customerId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Set<PetResponse> getByCustomerId(
            Principal authentication,
            @RequestParam("customerId") UUID customerId) {
        return businessService.getByCustomerId(authentication, customerId);
    }

    @Operation(summary = "Serviço de recuperação das informações do pet pelo id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do(s) pet(s). Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/pets/{petId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro do Pet não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Pet não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/pets/{petId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{petId}")
    @ResponseStatus(HttpStatus.OK)
    public PetResponse getById(
            Principal authentication,
            @PathVariable("petId") UUID petId) {
        return businessService.getById(authentication, petId);
    }

    @Operation(summary = "Serviço de desativação de pet.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao excluir pet. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/pets/{petId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro do Pet não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Pet não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/pets/{petId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/{petId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    public PetResponse deactivate (
            Principal authentication,
            @PathVariable("petId") UUID petId,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"/active\",\n" +
                    "        \"value\": \"false\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return businessService.deactivate(authentication, petId, patch);
    }
}
