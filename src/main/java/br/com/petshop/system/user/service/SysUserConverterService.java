package br.com.petshop.system.user.service;

import br.com.petshop.system.access.model.dto.response.AccessGroupResponse;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import br.com.petshop.system.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import br.com.petshop.system.user.model.dto.request.SysUserUpdateRequest;
import br.com.petshop.system.user.model.dto.response.SysUserResponse;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserConverterService {

    @Autowired
    private ModelMapper mapper;

    public SysUserEntity createRequestIntoEntity(SysUserCreateRequest request) {
        return mapper.map(request, SysUserEntity.class);
    }
    public SysUserEntity updateRequestIntoEntity(SysUserUpdateRequest request, SysUserEntity entity) {
        SysUserEntity newEntity = mapper.map(request, SysUserEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }
    public SysUserResponse entityIntoResponse(SysUserEntity entity) {
        SysUserResponse response = mapper.map(entity, SysUserResponse.class);
        if (response.getEmployee() != null) {
            EmployeeResponse employeeResponse = mapper.map(entity.getEmployee(), EmployeeResponse.class);
            response.setEmployee(employeeResponse);
        }
        return response;
    }
}
