package br.com.petshop.pet.service;

import br.com.petshop.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.pet.model.dto.request.PetUpdateRequest;
import br.com.petshop.pet.model.dto.response.PetResponse;
import br.com.petshop.pet.model.entity.PetEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe responsável pelas conversões de objetos do pet
 */
@Service
public class PetConverterService {

    @Autowired private ModelMapper mapper;

    /**
     * Método que converte dto (createRequest) em entidade
     * @param request - dto com dados de criação
     * @return - entidade
     */
    public PetEntity createRequestIntoEntity(PetCreateRequest request) {
        return mapper.map(request, PetEntity.class);
    }

    /**
     * Método que converte dto (updateRequest) em entidade
     * @param request - dto com dados de atualização
     * @return - entidade
     */
    public PetEntity updateRequestIntoEntity(PetUpdateRequest request) {
        return mapper.map(request, PetEntity.class);
    }

    /**
     * Método que faz merge de duas entidades
     * @param request - entidade com dados de atualização
     * @param entity - entidade atualmente salva em banco
     * @return - entidade
     */
    public PetEntity updateRequestIntoEntity(PetEntity request, PetEntity entity) {
        PetEntity newEntity = mapper.map(request, PetEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public PetResponse entityIntoResponse(PetEntity entity) {
        return mapper.map(entity, PetResponse.class);
    }
}
