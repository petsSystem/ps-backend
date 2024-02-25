package br.com.petshop.company.service;

import br.com.petshop.company.model.dto.response.CompanyResponse;
import br.com.petshop.company.model.dto.response.CompanySummaryResponse;
import br.com.petshop.company.model.entity.CompanyEntity;
import br.com.petshop.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.company.model.dto.response.CompanyTableResponse;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe responsável pelas conversões de objetos da loja/petshop
 */
@Service
public class CompanyConverterService {

    @Autowired private ModelMapper mapper;

    /**
     * Método que converte dto (createRequest) em entidade
     * @param request - dto com dados de criação
     * @return - entidade
     */
    public CompanyEntity createRequestIntoEntity(CompanyCreateRequest request) {
        request.setCnpj(request.getCnpj().replaceAll("[^0-9]", ""));
        return mapper.map(request, CompanyEntity.class);
    }

    /**
     * Método que converte dto (updateRequest) em entidade
     * @param request - dto com dados de atualização
     * @return - entidade
     */
    public CompanyEntity updateRequestIntoEntity(CompanyUpdateRequest request) {
        return mapper.map(request, CompanyEntity.class);
    }

    /**
     * Método que faz merge de duas entidades
     * @param request - entidade com dados de atualização
     * @param entity - entidade atualmente salva em banco
     * @return - entidade
     */
    public CompanyEntity updateRequestIntoEntity(CompanyEntity request, CompanyEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        return entity;
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public CompanyResponse entityIntoResponse(CompanyEntity entity) {
        return mapper.map(entity, CompanyResponse.class);
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public CompanyTableResponse entityIntoTableResponse(CompanyEntity entity) {
        return mapper.map(entity, CompanyTableResponse.class);
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public CompanySummaryResponse entityIntoAppResponse(CompanyEntity entity) {
        return mapper.map(entity, CompanySummaryResponse.class);
    }
}
