package br.com.petshop.appointment.controller;

import br.com.petshop.appointment.model.dto.request.AppointmentCreateRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentUpdateRequest;
import br.com.petshop.appointment.model.dto.response.AppointmentResponse;
import br.com.petshop.appointment.model.dto.response.AppointmentTableResponse;
import br.com.petshop.appointment.service.AppointmentBusinessService;
import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/api/v1/pet/appointments")
@Tag(name = "Appointment Services")
public class AppointmentController {

    @Autowired private AppointmentBusinessService businessService;

//    @Operation(summary = "Serviço de inclusão de agendamento para agenda informada.",
//            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Erro no sistema.",
//                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
//                            "\"type\": \"about:blank\",\n" +
//                            "\"title\": \"Bad Request\",\n" +
//                            "\"status\": 400,\n" +
//                            "\"detail\": \"Erro ao cadastrar agenda. Tente novamente mais tarde.\",\n" +
//                            "\"instance\": \"/api/v1/pet/schedules\"\n" +
//                            "}\n" +
//                            "\n")})}),
//            @ApiResponse(
//                    responseCode = "422",
//                    description = "Agenda já cadastrada.",
//                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
//                            "\"type\": \"about:blank\",\n" +
//                            "\"title\": \"Unprocessable Entity\",\n" +
//                            "\"status\": 422,\n" +
//                            "\"detail\": \"Agenda já cadastrada.\",\n" +
//                            "\"instance\": \"/api/v1/pet/schedules\"\n" +
//                            "}\n" +
//                            "\n")})}),
//    })
//    @PostMapping()
//    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
//    public AppointmentResponse create(
//            Principal authentication,
//            @RequestBody AppointmentCreateRequest request) {
//        return facade.create(authentication, request);
//    }
//
//    @Operation(summary = "Serviço de atualização de agenda pelo id.",
//            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Erro no sistema.",
//                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
//                            "\"type\": \"about:blank\",\n" +
//                            "\"title\": \"Bad Request\",\n" +
//                            "\"status\": 400,\n" +
//                            "\"detail\": \"Erro ao atualizar agenda. Tente novamente mais tarde.\",\n" +
//                            "\"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
//                            "}\n" +
//                            "\n")})}),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Agenda não encontrada.",
//                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
//                            "    \"type\": \"about:blank\",\n" +
//                            "    \"title\": \"Not Found\",\n" +
//                            "    \"status\": 404,\n" +
//                            "    \"detail\": \"Agenda não encontrada.\",\n" +
//                            "    \"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
//                            "}\n" +
//                            "\n")})})
//    })
//    @PutMapping("/{scheduleId}")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
//    public AppointmentResponse updateById(
//            Principal authentication,
//            @PathVariable("scheduleId") UUID scheduleId,
//            @RequestBody AppointmentUpdateRequest request) {
//        return facade.updateById(authentication, scheduleId, request);
//    }
//
//    @Operation(summary = "Serviço de ativação/desativação de agenda no sistema.",
//            description = "Acesso: 'ADMIN', 'OWNER', 'MANAGER'")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Erro no sistema.",
//                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
//                            "\"type\": \"about:blank\",\n" +
//                            "\"title\": \"Bad Request\",\n" +
//                            "\"status\": 400,\n" +
//                            "\"detail\": \"Erro ao ativar/desativar agenda. Tente novamente mais tarde.\",\n" +
//                            "\"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
//                            "}\n" +
//                            "\n")})}),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Agenda não encontrada.",
//                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
//                            "    \"type\": \"about:blank\",\n" +
//                            "    \"title\": \"Not Found\",\n" +
//                            "    \"status\": 404,\n" +
//                            "    \"detail\": \"Agenda não encontrada.\",\n" +
//                            "    \"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
//                            "}\n" +
//                            "\n")})})
//    })
//    @PatchMapping(path = "/{scheduleId}", consumes = "application/json-patch+json")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
//    public AppointmentResponse activate (
//            Principal authentication,
//            @PathVariable("scheduleId") UUID scheduleId,
//            @Schema(example = "[\n" +
//                    "    {\n" +
//                    "        \"op\": \"replace\",\n" +
//                    "        \"path\": \"/active\",\n" +
//                    "        \"value\": \"true\"\n" +
//                    "    }\n" +
//                    "]")
//            @RequestBody JsonPatch patch) {
//        return facade.activate(authentication, scheduleId, patch);
//    }
//
//
//
//    @Operation(summary = "Serviço de recuperação das agendas pelo id do produto.",
//            description = "Acesso: ALL")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Erro no sistema.",
//                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
//                            "\"type\": \"about:blank\",\n" +
//                            "\"title\": \"Bad Request\",\n" +
//                            "\"status\": 400,\n" +
//                            "\"detail\": \"Erro ao recuperar agenda(s). Tente novamente mais tarde.\",\n" +
//                            "\"instance\": \"/api/v1/pet/schedules?productId=\"\n" +
//                            "}\n" +
//                            "\n")})})
//    })
//    @GetMapping()
//    @ResponseStatus(HttpStatus.OK)
//    public List<AppointmentTableResponse> getByProductId (
//            Principal authentication,
//            @RequestParam("productId") UUID productId) {
//        return facade.getByProductId(authentication, productId);
//    }
//
//    @Operation(summary = "Serviço de recuperação das informações da agenda pelo id.",
//            description = "Acesso: ALL")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Erro no sistema.",
//                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
//                            "\"type\": \"about:blank\",\n" +
//                            "\"title\": \"Bad Request\",\n" +
//                            "\"status\": 400,\n" +
//                            "\"detail\": \"Erro ao recuperar agenda(s). Tente novamente mais tarde.\",\n" +
//                            "\"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
//                            "}\n" +
//                            "\n")})}),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Agenda não encontrada.",
//                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
//                            "    \"type\": \"about:blank\",\n" +
//                            "    \"title\": \"Not Found\",\n" +
//                            "    \"status\": 404,\n" +
//                            "    \"detail\": \"Agenda não encontrada.\",\n" +
//                            "    \"instance\": \"/api/v1/pet/schedules/{scheduleId}\"\n" +
//                            "}\n" +
//                            "\n")})})
//    })
//    @GetMapping("/{scheduleId}")
//    @ResponseStatus(HttpStatus.OK)
//    public AppointmentResponse getById (
//            Principal authentication,
//            @PathVariable("scheduleId") UUID scheduleId) {
//        return facade.getById(authentication, scheduleId);
//    }
}
