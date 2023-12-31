package br.com.petshop.app.pet.controller;

import br.com.petshop.app.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.app.pet.model.dto.request.PetUpdateRequest;
import br.com.petshop.app.pet.service.PetService;
import br.com.petshop.app.pet.model.dto.response.PetResponse;
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
@RequestMapping("/api/v1/app/pets")
@Tag(name = "APP - Pets Services")
public class PetController {

    @Autowired private PetService petService;

    @Operation(summary = "Serviço de inclusão do Pet do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "422",
                    description = "Pet já cadastrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Pet {nome_pet} já cadastrado(a) no sistema.\",\n" +
                            "\"instance\": \"/api/v1/app/pets\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar pet. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/pets\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public PetResponse create(Principal authentication,
                              @RequestBody PetCreateRequest request) {
        return petService.create(authentication, request);
    }

    @Operation(summary = "Serviço de atualização do cadastro do Pet do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro do Pet não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de pet não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/app/pets/{pet_id}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados do pet. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/pets/{pet_id}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{petId}")
    @ResponseStatus(HttpStatus.OK)
    public PetResponse update(
            Principal authentication,
            @PathVariable("petId") String petId,
            @RequestBody PetUpdateRequest request) {
        return petService.update(authentication, petId, request);
    }

    @Operation(summary = "Serviço de recuperação das informações cadastradas do Pet do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro do Pet não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de pet não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/app/pets/{pet_id}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do pet. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/pets/{pet_id}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Set<PetResponse> get(
            Principal authentication) {
        return petService.get(authentication);
    }

    @Operation(summary = "Serviço de recuperação das informações cadastradas do Pet do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro do Pet não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de pet não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/app/pets/{pet_id}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do pet. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/pets/{pet_id}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{petId}")
    @ResponseStatus(HttpStatus.OK)
    public PetResponse getById(
            Principal authentication,
            @PathVariable("petId") String petId) {
        return petService.getById(authentication, petId);
    }

    @Operation(summary = "Serviço de exclusão do cadastro do Pet do usuário no APP.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro do Pet não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de pet não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/app/pets/{pet_id}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados do pet. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/app/pets/{pet_id}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @DeleteMapping("/{petId}")
    @ResponseStatus(HttpStatus.OK)
    public void deactivate(
            Principal authentication,
            @PathVariable("petId") String petId) {
        petService.deactivate(authentication, petId);
    }
}
