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

@Service
public class AppointmentConverterService {

    @Autowired
    private ModelMapper mapper;

//    public AppointmentEntity createRequestIntoEntity(AppointmentCreateRequest request) {
//        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        return mapper.map(request, AppointmentEntity.class);
//    }
//
//    public AppointmentEntity updateRequestIntoEntity(AppointmentUpdateRequest request) {
//        AppointmentEntity entity = mapper.map(request, AppointmentEntity.class);
//        entity.setDays(request.getDays());
//        return entity;
//    }
//
//    public AppointmentEntity updateRequestIntoEntity(AppointmentEntity request, AppointmentEntity entity) {
//        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
//        mapper.map(request, entity);
//        entity.setDays(request.getDays());
//        return entity;
//    }
//
//    public AppointmentResponse entityIntoResponse(AppointmentEntity entity) {
//        return mapper.map(entity, AppointmentResponse.class);
//    }
//
//    public AppointmentTableResponse entityIntoTableResponse(AppointmentEntity entity) {
//        return mapper.map(entity, AppointmentTableResponse.class);
//    }
}
