package br.com.petshop.system.schedule.service;

import br.com.petshop.system.schedule.model.dto.request.ScheduleCreateRequest;
import br.com.petshop.system.schedule.model.dto.request.ScheduleUpdateRequest;
import br.com.petshop.system.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.system.schedule.model.entity.ScheduleEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleConverterService {

    @Autowired
    private ModelMapper mapper;

    public ScheduleEntity createRequestIntoEntity(ScheduleCreateRequest request) {
        return mapper.map(request, ScheduleEntity.class);
    }

    public ScheduleEntity updateRequestIntoEntity(ScheduleUpdateRequest request, ScheduleEntity entity) {
        ScheduleEntity newEntity = mapper.map(request, ScheduleEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }

    public ScheduleResponse entityIntoResponse(ScheduleEntity entity) {
        return mapper.map(entity, ScheduleResponse.class);
    }
}
