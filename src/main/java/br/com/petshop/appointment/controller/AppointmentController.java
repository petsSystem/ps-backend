package br.com.petshop.appointment.controller;

import br.com.petshop.appointment.model.dto.request.AppointmentCreateRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentStatusRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentUpdateRequest;
import br.com.petshop.appointment.model.dto.response.AppointmentResponse;
import br.com.petshop.appointment.model.dto.response.AppointmentTableResponse;
import br.com.petshop.appointment.service.AppointmentBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Classe responsável pelos endpoints de agendamento.
 */
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
            @RequestBody @Valid AppointmentCreateRequest request) {
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
                            "\"detail\": \"Erro ao alterar status do agendamento. Tente novamente mais tarde.\",\n" +
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
    @PatchMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse setStatus (
            Principal authentication,
            @RequestBody AppointmentStatusRequest request) {
        return businessService.setStatus(authentication, request);
    }

    @Operation(summary = "Serviço de recuperação da disponibilidade dos dias dos próximos 3 meses.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar disponibilidade de agendamento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/appointments?companyId=&productId=&userId=\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/month")
    @ResponseStatus(HttpStatus.OK)
    public TreeMap<LocalDate, Boolean> getMonthAvailability (
            Principal authentication,
            @RequestParam(value = "companyId", required = true) UUID companyId,
            @RequestParam(value = "productId", required = true) UUID productId,
            @RequestParam(value = "userId", required = false) UUID userId) {
        AppointmentFilterRequest filter = AppointmentFilterRequest.builder()
                .companyId(companyId)
                .productId(productId)
                .userId(userId)
                .build();
        return businessService.getMonthAvailability(authentication, filter);
    }

    @Operation(summary = "Serviço de recuperação da disponibilidade do dia informado.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar disponibilidade de agendamento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/appointments?companyId=&productId=&userId=\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/day")
    @ResponseStatus(HttpStatus.OK)
    public TreeMap<LocalTime, Boolean> getDayAvailability (
            Principal authentication,
            @RequestParam(value = "date", required = true) String date,
            @RequestParam(value = "companyId", required = true) UUID companyId,
            @RequestParam(value = "productId", required = true) UUID productId,
            @RequestParam(value = "userId", required = false) UUID userId) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateFormatted = LocalDate.parse(date, dateFormatter);

        AppointmentFilterRequest filter = AppointmentFilterRequest.builder()
                .companyId(companyId)
                .productId(productId)
                .userId(userId)
                .date(dateFormatted)
                .build();

        return businessService.getDayAvailability(authentication, filter);
    }

    @Operation(summary = "Serviço de recuperação da disponibilidade do dia informado.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar disponibilidade de agendamento. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/pet/appointments?companyId=&productId=&userId=\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/schedule")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentTableResponse> schedule (
            Principal authentication,
            @RequestParam(value = "companyId", required = true) UUID companyId,
            @RequestParam(value = "categoryId", required = false) UUID categoryId,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "productId", required = false) UUID productId,
            @RequestParam(value = "userId", required = false) UUID userId) {

        AppointmentFilterRequest filter = AppointmentFilterRequest.builder()
                .companyId(companyId)
                .categoryId(categoryId)
                .productId(productId)
                .userId(userId)
                .build();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (Objects.nonNull(date))
            filter.setDate(LocalDate.parse(date, dateFormatter));

        return businessService.schedule(authentication, filter);
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
                            "\"detail\": \"Erro ao recuperar agendamento(s). Tente novamente mais tarde.\",\n" +
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
    @GetMapping("/{appointmentId}")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse getById (
            Principal authentication,
            @PathVariable("scheduleId") UUID appointmentId) {
        return businessService.getById(authentication, appointmentId);
    }
}
