package br.com.petshop.appointment.controller;

import br.com.petshop.appointment.model.dto.request.AppointmentCreateRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentStatusRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentUpdateRequest;
import br.com.petshop.appointment.model.dto.response.AppointmentResponse;
import br.com.petshop.appointment.service.AppointmentBusinessService;
import br.com.petshop.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.schedule.model.dto.response.ScheduleTableResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pet/appointments")
@Tag(name = "Appointment Services")
public class AppointmentController {

    @Autowired private AppointmentBusinessService businessService;

    @Operation(summary = "Serviço de inclusão de agendamento para agenda informada.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao cadastrar agendamento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/appointments\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(
            Principal authentication,
            @RequestBody AppointmentCreateRequest request) {
        return businessService.create(authentication, request);
    }

    @Operation(summary = "Serviço de atualização de agendamento pelo id.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar agendamento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/appointments/{appointmentId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Agendamento não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/appointments/{appointmentId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse updateById(
            Principal authentication,
            @PathVariable("scheduleId") UUID scheduleId,
            @RequestBody AppointmentUpdateRequest request) {
        return businessService.updateById(authentication, scheduleId, request);
    }

    @Operation(summary = "Serviço de atualização de status do agendamento.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao alterar status do agendamento agenda. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/appointments/{appointmentId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Agendamento não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/pet/appointments/{appointmentId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse setStatus (
            Principal authentication,
            @RequestBody AppointmentStatusRequest request) {
        return businessService.setStatus(authentication, request);
    }

    @Operation(summary = "Serviço de recuperação dos agendamentos por filtro.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar agendamento(s). Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/appointments?productId=\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<AppointmentResponse>> getByFilter (
            Principal authentication,
            @RequestParam(value = "companyId", required = true) UUID companyId,
            @RequestParam(value = "productId", required = true) UUID productId,
            @RequestParam(value = "userId", required = false) UUID userId) {
        AppointmentFilterRequest filter = AppointmentFilterRequest.builder()
                .companyId(companyId)
                .productId(productId)
                .userId(userId)
                .build();
        return businessService.getByFilter(authentication, filter);
    }



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
