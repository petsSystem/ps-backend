package br.com.petshop.schedule.service;

import br.com.petshop.schedule.model.dto.request.ScheduleCreateRequest;
import br.com.petshop.schedule.model.dto.request.ScheduleUpdateRequest;
import br.com.petshop.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.schedule.model.dto.response.ScheduleTableResponse;
import br.com.petshop.schedule.model.entity.ScheduleEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleConverterService {

    @Autowired
    private ModelMapper mapper;

    public ScheduleEntity createRequestIntoEntity(ScheduleCreateRequest request) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(request, ScheduleEntity.class);
    }

    public ScheduleEntity updateRequestIntoEntity(ScheduleUpdateRequest request) {
        ScheduleEntity entity = mapper.map(request, ScheduleEntity.class);
        entity.setDays(request.getDays());
        return entity;
    }

    public ScheduleEntity updateRequestIntoEntity(ScheduleEntity request, ScheduleEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        entity.setDays(request.getDays());
        return entity;
    }

    public ScheduleResponse entityIntoResponse(ScheduleEntity entity) {
        return mapper.map(entity, ScheduleResponse.class);
    }

    public ScheduleTableResponse entityIntoTableResponse(ScheduleEntity entity) {
        return mapper.map(entity, ScheduleTableResponse.class);
    }
}
