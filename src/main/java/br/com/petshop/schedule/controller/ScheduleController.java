package br.com.petshop.schedule.controller;

import br.com.petshop.schedule.model.dto.request.ScheduleCreateRequest;
import br.com.petshop.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.schedule.model.dto.request.ScheduleUpdateRequest;
import br.com.petshop.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.schedule.service.ScheduleBusinessService;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.UUID;

/**
 * Classe responsável pelos endpoints de agenda.
 */
@RestController
@RequestMapping("/api/v1/pet/schedules")
@Tag(name = "Schedule Services")
public class ScheduleController {

    @Autowired private ScheduleBusinessService businessService;

    @Operation(summary = "Serviço de inclusão de agenda para funcionário no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar agenda. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/schedules\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Agenda já cadastrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Agenda já cadastrada.\",\n" +
                            "\"instance\": \"/api/v1/pet/schedules\"\n" +
                            "}\n" +
                            "\n")})}),
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public ScheduleResponse create(
            Principal authentication,
            @RequestBody @Valid ScheduleCreateRequest request) {
        return businessService.create(authentication, request);
    }

    @Operation(summary = "Serviço de atualização de agenda pelo id.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar agenda. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agenda não encontrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Agenda não encontrada.\",\n" +
                            "    \"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public ScheduleResponse updateById(
            Principal authentication,
            @PathVariable("scheduleId") UUID scheduleId,
            @RequestBody ScheduleUpdateRequest request) {
        return businessService.updateById(authentication, scheduleId, request);
    }

    @Operation(summary = "Serviço de ativação/desativação de agenda no sistema.",
            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao ativar/desativar agenda. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agenda não encontrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Agenda não encontrada.\",\n" +
                            "    \"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/{scheduleId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
    public ScheduleResponse activate (
            Principal authentication,
            @PathVariable("scheduleId") UUID scheduleId,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"/active\",\n" +
                    "        \"value\": \"true\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return businessService.activate(authentication, scheduleId, patch);
    }

    @Operation(summary = "Serviço de recuperação das agendas por filtro.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar agenda(s). Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/schedules?productId=\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponse> getByFilter (
            Principal authentication,
            @RequestParam(value = "companyId", required = true) UUID companyId,
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestParam(value = "productId", required = false) UUID productId) {
        ScheduleFilterRequest filter = ScheduleFilterRequest.builder()
                .companyId(companyId)
                .productId(productId)
                .userId(userId)
                .build();
        return businessService.getByFilter(authentication, filter);
    }

    @Operation(summary = "Serviço de recuperação das informações da agenda pelo id.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar agenda(s). Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agenda não encontrada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Agenda não encontrada.\",\n" +
                            "    \"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse getById (
            Principal authentication,
            @PathVariable("scheduleId") UUID scheduleId) {
        return businessService.getById(authentication, scheduleId);
    }
}
