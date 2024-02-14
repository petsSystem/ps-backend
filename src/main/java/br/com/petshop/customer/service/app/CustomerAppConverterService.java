package br.com.petshop.customer.service.app;

import br.com.petshop.customer.model.dto.request.app.CustomerAppCreateRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerAppUpdateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.entity.CustomerEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerAppConverterService {

    @Autowired
    private ModelMapper mapper;

    public CustomerEntity createRequestIntoEntity(CustomerAppCreateRequest request) {
        request.setCpf(request.getCpf().replaceAll("[^0-9]", ""));
        return mapper.map(request, CustomerEntity.class);
    }

    public CustomerEntity updateRequestIntoEntity(CustomerAppUpdateRequest request) {
        return mapper.map(request, CustomerEntity.class);
    }

    public CustomerEntity updateRequestIntoEntity(CustomerEntity request, CustomerEntity entity) {
        CustomerEntity newEntity = mapper.map(request, CustomerEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }

    public CustomerResponse entityIntoResponse(CustomerEntity entity) {
        CustomerResponse response = mapper.map(entity, CustomerResponse.class);
        return response;
    }
}
