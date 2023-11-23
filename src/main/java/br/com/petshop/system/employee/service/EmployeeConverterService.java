package br.com.petshop.system.employee.service;

import br.com.petshop.system.employee.model.dto.request.EmployeeCreateRequest;
import br.com.petshop.system.employee.model.dto.request.EmployeeUpdateRequest;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeConverterService {

    @Autowired
    private ModelMapper mapper;

    public EmployeeEntity createRequestIntoEntity(EmployeeCreateRequest request) {
        return mapper.map(request, EmployeeEntity.class);
    }

    public EmployeeEntity updateRequestIntoEntity(EmployeeUpdateRequest request) {
        return mapper.map(request, EmployeeEntity.class);
    }

    public EmployeeEntity updateRequestIntoEntity(EmployeeEntity request, EmployeeEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        return entity;
    }

    public EmployeeResponse entityIntoResponse(EmployeeEntity entity) {
        return mapper.map(entity, EmployeeResponse.class);
    }
}
