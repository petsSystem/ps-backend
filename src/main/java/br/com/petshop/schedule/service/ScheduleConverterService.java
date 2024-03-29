package br.com.petshop.schedule.service;

import br.com.petshop.schedule.model.dto.request.ScheduleCreateRequest;
import br.com.petshop.schedule.model.dto.request.ScheduleUpdateRequest;
import br.com.petshop.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.schedule.model.entity.ScheduleEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe responsável pelas conversões de objetos da agenda
 */
@Service
public class ScheduleConverterService {

    @Autowired private ModelMapper mapper;

    /**
     * Método que converte dto (createRequest) em entidade
     * @param request - dto com dados de criação
     * @return - entidade
     */
    public ScheduleEntity createRequestIntoEntity(ScheduleCreateRequest request) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ScheduleEntity entity = mapper.map(request, ScheduleEntity.class);
        entity.setProductIds(request.getProductIds());
        return entity;
    }

    /**
     * Método que converte dto (updateRequest) em entidade
     * @param request - dto com dados de atualização
     * @return - entidade
     */
    public ScheduleEntity updateRequestIntoEntity(ScheduleUpdateRequest request) {
        ScheduleEntity entity = mapper.map(request, ScheduleEntity.class);
        entity.setDays(request.getDays());
        return entity;
    }

    /**
     * Método que faz merge de duas entidades
     * @param request - entidade com dados de atualização
     * @param entity - entidade atualmente salva em banco
     * @return - entidade
     */
    public ScheduleEntity updateRequestIntoEntity(ScheduleEntity request, ScheduleEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        entity.setDays(request.getDays());
        entity.setProductIds(request.getProductIds());
        return entity;
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public ScheduleResponse entityIntoResponse(ScheduleEntity entity) {
        return mapper.map(entity, ScheduleResponse.class);
    }
}
