package br.com.petshop.user.service;

import br.com.petshop.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.user.model.dto.request.SysUserUpdateProfileRequest;
import br.com.petshop.user.model.dto.request.SysUserUpdateRequest;
import br.com.petshop.user.model.dto.response.SysUserMeResponse;
import br.com.petshop.user.model.dto.response.SysUserProfileResponse;
import br.com.petshop.user.model.dto.response.SysUserResponse;
import br.com.petshop.user.model.dto.response.SysUserTableResponse;
import br.com.petshop.user.model.entity.UserEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe responsável pelas conversões de objetos do usuário do sistema web.
 */
@Service
public class SysUserConverterService {

    @Autowired private ModelMapper mapper;

    /**
     * Método que converte dto (createRequest) em entidade
     * @param request - dto com dados de criação
     * @return - entidade
     */
    public UserEntity createRequestIntoEntity(SysUserCreateRequest request) {
        request.setCpf(request.getCpf().replaceAll("[^0-9]", ""));
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity entity = mapper.map(request, UserEntity.class);
        entity.setCompanyIds(request.getCompanyIds());
        entity.setProfileIds(request.getProfileIds());

        return entity;
    }

    /**
     * Método que converte dto (updateRequest) em entidade
     * @param request - dto com dados de atualização
     * @return - entidade
     */
    public UserEntity updateRequestIntoEntity(SysUserUpdateRequest request) {
        return mapper.map(request, UserEntity.class);
    }

    /**
     * Método que faz merge de duas entidades
     * @param request - entidade com dados de atualização
     * @param entity - entidade atualmente salva em banco
     * @return - entidade
     */
    public UserEntity updateRequestIntoEntity(UserEntity request, UserEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        entity.setCompanyIds(request.getCompanyIds());
        entity.setProfileIds(request.getProfileIds());
        return entity;
    }

    /**
     * Método que converte dto (updateRequest) em entidade
     * @param request - dto com dados de atualização
     * @return - entidade
     */
    public UserEntity updateProfileRequestIntoEntity(SysUserUpdateProfileRequest request) {
        return mapper.map(request, UserEntity.class);
    }

    /**
     * Método que faz merge de duas entidades
     * @param request - entidade com dados de atualização
     * @param entity - entidade atualmente salva em banco
     * @return - entidade
     */
    public UserEntity updateProfileRequestIntoEntity(UserEntity request, UserEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        return entity;
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public SysUserResponse entityIntoResponse(UserEntity entity) {
        return mapper.map(entity, SysUserResponse.class);
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public SysUserTableResponse entityIntoTableResponse(UserEntity entity) {
        return mapper.map(entity, SysUserTableResponse.class);
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public SysUserProfileResponse entityIntoProfileResponse(UserEntity entity) {
        return mapper.map(entity, SysUserProfileResponse.class);
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public SysUserMeResponse entityIntoMeResponse(UserEntity entity) {
        return mapper.map(entity, SysUserMeResponse.class);
    }
}
