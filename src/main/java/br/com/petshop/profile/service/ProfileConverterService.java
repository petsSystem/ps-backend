package br.com.petshop.profile.service;

import br.com.petshop.profile.model.dto.request.ProfileCreateRequest;
import br.com.petshop.profile.model.dto.request.ProfileUpdateRequest;
import br.com.petshop.profile.model.dto.response.ProfileLabelResponse;
import br.com.petshop.profile.model.dto.response.ProfileResponse;
import br.com.petshop.profile.model.entity.ProfileEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe responsável pelas conversões de objetos do perfil do usuário do sistema.
 */
@Service
public class ProfileConverterService {

    @Autowired private ModelMapper mapper;

    /**
     * Método que converte dto (createRequest) em entidade
     * @param request - dto com dados de criação
     * @return - entidade
     */
    public ProfileEntity createRequestIntoEntity(ProfileCreateRequest request) {
        return mapper.map(request, ProfileEntity.class);
    }

    /**
     * Método que converte dto (updateRequest) em entidade
     * @param request - dto com dados de atualização
     * @return - entidade
     */
    public ProfileEntity updateRequestIntoEntity(ProfileUpdateRequest request) {
        return mapper.map(request, ProfileEntity.class);
    }

    /**
     * Método que faz merge de duas entidades
     * @param request - entidade com dados de atualização
     * @param entity - entidade atualmente salva em banco
     * @return - entidade
     */
    public ProfileEntity updateRequestIntoEntity(ProfileEntity request, ProfileEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        entity.setPermissions(request.getPermissions());
        return entity;
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public ProfileResponse entityIntoResponse(ProfileEntity entity) {
        return mapper.map(entity, ProfileResponse.class);
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public ProfileLabelResponse entityIntoLabelResponse(ProfileEntity entity) {
        return mapper.map(entity, ProfileLabelResponse.class);
    }
}
