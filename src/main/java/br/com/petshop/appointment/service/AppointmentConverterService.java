package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.dto.request.AppointmentCreateRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentUpdateRequest;
import br.com.petshop.appointment.model.dto.response.AppointmentResponse;
import br.com.petshop.appointment.model.entity.AppointmentEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe responsável pelas conversões de objetos do agendamento
 */
@Service
public class AppointmentConverterService {

    @Autowired private ModelMapper mapper;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Método que converte dto (createRequest) em entidade
     * @param request - dto com dados de criação
     * @return - entidade
     */
    public AppointmentEntity createRequestIntoEntity(AppointmentCreateRequest request) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        AppointmentEntity entity = mapper.map(request, AppointmentEntity.class);

        String date = request.getDate().replaceAll("/", "-");
        entity.setDate(LocalDate.parse(date, dateFormatter));
        entity.setTime(LocalTime.parse(request.getTime(), timeFormatter));

        return entity;
    }

    /**
     * Método que converte dto (updateRequest) em entidade
     * @param request - dto com dados de atualização
     * @return - entidade
     */
    public AppointmentEntity updateRequestIntoEntity(AppointmentUpdateRequest request) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        AppointmentEntity entity = mapper.map(request, AppointmentEntity.class);
        entity.setAdditionalIds(request.getAdditionalIds());

        String date = request.getDate().replaceAll("/", "-");
        entity.setDate(LocalDate.parse(date, dateFormatter));
        entity.setTime(LocalTime.parse(request.getTime(), timeFormatter));

        return entity;
    }

    /**
     * Método que faz merge de duas entidades
     * @param request - entidade com dados de atualização
     * @param entity - entidade atualmente salva em banco
     * @return - entidade
     */
    public AppointmentEntity updateRequestIntoEntity(AppointmentEntity request, AppointmentEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        entity.setAdditionalIds(request.getAdditionalIds());
        return entity;
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public AppointmentResponse entityIntoResponse(AppointmentEntity entity) {
        ;AppointmentResponse appointment =  mapper.map(entity, AppointmentResponse.class);

        String date = entity.getDate().format(dateFormatter);
        date = date.replaceAll("-", "/");
        String time = entity.getTime().format(timeFormatter);
        appointment.setDate(date);
        appointment.setTime(time);

        return appointment;
    }
}
