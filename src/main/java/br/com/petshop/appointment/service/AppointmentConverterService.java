package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.dto.request.AppointmentCreateRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentUpdateRequest;
import br.com.petshop.appointment.model.dto.response.AppointmentResponse;
import br.com.petshop.appointment.model.dto.response.AppointmentTableResponse;
import br.com.petshop.appointment.model.entity.AppointmentEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class AppointmentConverterService {

    @Autowired private ModelMapper mapper;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public AppointmentEntity createRequestIntoEntity(AppointmentCreateRequest request) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(request, AppointmentEntity.class);
    }

    public AppointmentEntity updateRequestIntoEntity(AppointmentUpdateRequest request) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        AppointmentEntity entity = mapper.map(request, AppointmentEntity.class);
        entity.setAdditionalIds(request.getAdditionalIds());
        return entity;
    }

    public AppointmentEntity updateRequestIntoEntity(AppointmentEntity request, AppointmentEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        entity.setAdditionalIds(request.getAdditionalIds());
        return entity;
    }

    public AppointmentResponse entityIntoResponse(AppointmentEntity entity) {
        AppointmentResponse appointment =  mapper.map(entity, AppointmentResponse.class);
        appointment.setDate(LocalDate.parse(entity.getDate(), dateFormatter));
        appointment.setTime(LocalTime.parse(entity.getTime(), timeFormatter));

        return appointment;
    }

    public AppointmentTableResponse entityIntoTableResponse(AppointmentEntity entity) {
        return mapper.map(entity, AppointmentTableResponse.class);
    }
}
