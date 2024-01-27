package br.com.petshop.customer.service;

import br.com.petshop.customer.model.dto.request.CustomerAppCreateRequest;
import br.com.petshop.customer.model.dto.request.CustomerSysCreateRequest;
import br.com.petshop.customer.model.dto.request.CustomerSysUpdateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.dto.response.CustomerTableResponse;
import br.com.petshop.customer.model.entity.CustomerEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerConverterService {

    @Autowired private ModelMapper mapper;

    public CustomerEntity appCreateRequestIntoEntity(CustomerAppCreateRequest request) {
        request.setCpf(request.getCpf().replaceAll("[^0-9]", ""));
        return mapper.map(request, CustomerEntity.class);
    }

    public CustomerEntity sysCreateRequestIntoEntity(CustomerSysCreateRequest request) {
        request.setCpf(request.getCpf().replaceAll("[^0-9]", ""));
        return mapper.map(request, CustomerEntity.class);
    }

    public CustomerResponse entityIntoResponse(CustomerEntity entity) {
        CustomerResponse response = mapper.map(entity, CustomerResponse.class);
        return response;
    }

    public CustomerTableResponse entityIntoTableResponse(CustomerEntity entity) {
        return mapper.map(entity, CustomerTableResponse.class);
    }

    public CustomerEntity updateRequestIntoEntity(CustomerSysUpdateRequest request, CustomerEntity entity) {
        CustomerEntity newEntity = mapper.map(request, CustomerEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }
}
