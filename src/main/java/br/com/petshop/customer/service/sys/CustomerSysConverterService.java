package br.com.petshop.customer.service.sys;

import br.com.petshop.customer.model.dto.request.sys.CustomerSysCreateRequest;
import br.com.petshop.customer.model.dto.request.sys.CustomerSysUpdateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.dto.response.CustomerTableResponse;
import br.com.petshop.customer.model.entity.CustomerEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe responsável pelas conversões de objetos do cliente do sistema web.
 */
@Service
public class CustomerSysConverterService {

    @Autowired
    private ModelMapper mapper;

    /**
     * Método que converte dto (createRequest) em entidade
     * @param request - dto com dados de criação
     * @return - entidade
     */
    public CustomerEntity createRequestIntoEntity(CustomerSysCreateRequest request) {
        request.setCpf(request.getCpf().replaceAll("[^0-9]", ""));
        return mapper.map(request, CustomerEntity.class);
    }

    /**
     * Método que converte dto (updateRequest) em entidade
     * @param request - dto com dados de atualização
     * @return - entidade
     */
    public CustomerEntity updateRequestIntoEntity(CustomerSysUpdateRequest request) {
        return mapper.map(request, CustomerEntity.class);
    }

    /**
     * Método que faz merge de duas entidades
     * @param request - entidade com dados de atualização
     * @param entity - entidade atualmente salva em banco
     * @return - entidade
     */
    public CustomerEntity updateRequestIntoEntity(CustomerEntity request, CustomerEntity entity) {
        CustomerEntity newEntity = mapper.map(request, CustomerEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public CustomerResponse entityIntoResponse(CustomerEntity entity) {
        CustomerResponse response = mapper.map(entity, CustomerResponse.class);
        return response;
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public CustomerTableResponse entityIntoTableResponse(CustomerEntity entity) {
        return mapper.map(entity, CustomerTableResponse.class);
    }
}
