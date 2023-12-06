package br.com.petshop.system.schedule.controller;

import br.com.petshop.system.schedule.model.dto.request.ScheduleUpdateRequest;
import br.com.petshop.system.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.system.schedule.model.enums.ScheduleType;
import br.com.petshop.system.schedule.service.ScheduleService;
import br.com.petshop.system.schedule.service.ScheduleValidateService;
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
@RequestMapping("/api/v1/sys/schedules")
@Tag(name = "SYS - Schedules Services")
public class ScheduleController {

    @Autowired private ScheduleValidateService service;

    //ACESSO: ALL
    @Operation(summary = "Serviço que retorna as agendas de uma loja.",
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
                            "\"instance\": \"/api/v1/sys/schedules/company/{companyId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @GetMapping("/company/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponse> getByCompanyId(
            Principal authentication,
            @PathVariable("companyId") UUID companyId) {
        return service.getByCompanyId(authentication, companyId);
    }

//    //ACESSO: 'ADMIN', 'OWNER', 'MANAGER'
//    @Operation(summary = "Serviço de inclusão de agenda de serviço.",
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
//                            "\"instance\": \"/api/v1/system/schedules\"\n" +
//                            "}")})}),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Agenda não existe.",
//                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
//                            "    \"type\": \"about:blank\",\n" +
//                            "    \"title\": \"Not Found\",\n" +
//                            "    \"status\": 404,\n" +
//                            "    \"detail\": \"Agenda não existe.\",\n" +
//                            "    \"instance\": \"/api/v1/system/schedules\"\n" +
//                            "}\n" +
//                            "\n")})}),
//            @ApiResponse(
//                    responseCode = "422",
//                    description = "Agenda já cadastrada.",
//                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
//                            "\"type\": \"about:blank\",\n" +
//                            "\"title\": \"Unprocessable Entity\",\n" +
//                            "\"status\": 422,\n" +
//                            "\"detail\": \"Agenda já cadastrada no sistema.\",\n" +
//                            "\"instance\": \"/api/v1/system/schedules\"\n" +
//                            "}")})})
//    })
//    @PostMapping()
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'MANAGER')")
//    public ScheduleResponse create(Principal authentication,
//                                   @RequestBody ScheduleCreateRequest request) {
//        return scheduleService.create(authentication, request);
//    }

    @Operation(summary = "Serviço de atualização parcial de agenda no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar parcialmente os dados da agenda. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro de agenda não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de agenda não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PatchMapping(path = "/{scheduleId}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ScheduleResponse partialUpdate(
            @PathVariable("scheduleId") UUID scheduleId,
            @Schema(example = "[\n" +
                    "    {\n" +
                    "        \"op\": \"replace\",\n" +
                    "        \"path\": \"/active\",\n" +
                    "        \"value\": \"true\"\n" +
                    "    }\n" +
                    "]")
            @RequestBody JsonPatch patch) {
        return null;//scheduleService.partialUpdate(scheduleId, patch);
    }

    //ACESSO: ALL (COM FILTROS)
    @Operation(summary = "Serviço de recuperação das informações da agenda no sistema.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao recuperar dados da agenda. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/schedules\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Funcionário não pertence a empresa/loja informada.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Forbidden\",\n" +
                            "    \"status\": 403,\n" +
                            "    \"detail\": \"Acesso proibido.\",\n" +
                            "    \"instance\": \"/api/v1/sys/schedules\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "422",
                    description = "Funcionário com mais de uma agenda associada. Necessário selecionar uma empresa/loja.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Unprocessable Entity\",\n" +
                            "\"status\": 422,\n" +
                            "\"detail\": \"Funcionário com mais de uma agenda associada. Necessário selecionar uma empresa/loja.\",\n" +
                            "\"instance\": \"/api/v1/sys/schedules\"\n" +
                            "}")})})
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<ScheduleResponse> get(
            Principal authentication,
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) ScheduleType type,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        //ScheduleFilterRequest filter = new ScheduleFilterRequest(companyId, cpf, email, type, active);
        return null;// scheduleService.get(authentication, pageable, null);
    }



    //ACESSO: ALL
    @Operation(summary = "Serviço de atualização de qualquer agenda no sistema.",
            description = "Acesso: ALL")
    @ApiResponses(value = {
            @ApiResponse (
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao atualizar dados da agenda. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário não tem permissão para alterar dados da agenda.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Forbidden\",\n" +
                            "    \"status\": 403,\n" +
                            "    \"detail\": \"Acesso proibido.\",\n" +
                            "    \"instance\": \"/api/v1/sys/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cadastro de agenda não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de agenda não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @PutMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse updateById(
            Principal authentication,
            @PathVariable("scheduleId") UUID scheduleId,
            @RequestBody ScheduleUpdateRequest request) {
        return null;// scheduleService.updateById(authentication, scheduleId, request);
    }

    //ACESSO: 'ADMIN'
    @Operation(summary = "Serviço de exclusão do cadastro de agenda no sistema.",
            description = "Acesso: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Cadastro de agenda não encontrado.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "    \"type\": \"about:blank\",\n" +
                            "    \"title\": \"Not Found\",\n" +
                            "    \"status\": 404,\n" +
                            "    \"detail\": \"Cadastro de agenda não encontrado.\",\n" +
                            "    \"instance\": \"/api/v1/sys/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro no sistema.",
                    content = { @Content(examples = {@ExampleObject(value = "{\n" +
                            "\"type\": \"about:blank\",\n" +
                            "\"title\": \"Bad Request\",\n" +
                            "\"status\": 400,\n" +
                            "\"detail\": \"Erro ao excluir dados da agenda do sistema. Tente novamente mais tarde.\",\n" +
                            "\"instance\": \"/api/v1/sys/schedules/{scheduleId}\"\n" +
                            "}\n" +
                            "\n")})})
    })
    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete (
            @PathVariable("scheduleId") UUID scheduleId) {
        //scheduleService.delete(scheduleId);
    }
}
