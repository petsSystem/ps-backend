package br.com.petshop.system.user.service;

import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.access.service.AccessGroupService;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import br.com.petshop.system.employee.service.EmployeeService;
import br.com.petshop.system.user.model.dto.request.SysUserFilterRequest;
import br.com.petshop.system.user.model.dto.response.SysUserResponse;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import br.com.petshop.system.user.repository.SysUserRepository;
import br.com.petshop.system.user.repository.SysUserSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SysUserService {

    Logger log = LoggerFactory.getLogger(SysUserService.class);
    @Autowired private SysUserRepository systemUserRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SysUserAsyncService asyncService;
    @Autowired private EmployeeService employeeService;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private SysUserSpecification specification;
    @Autowired private AccessGroupService accessGroupService;

    public SysUserEntity create (SysUserEntity request, UUID employeeId) {
        EmployeeEntity employeeEntity = employeeService.findByIdAndActive(employeeId);
        request.setEmail(employeeEntity.getEmail());

        String password = generatePassword();

        request.setPassword(passwordEncoder.encode(password));
        request.setEmployee(employeeEntity);
        SysUserEntity entity = systemUserRepository.save(request);

        asyncService.sendNewPassword(entity, password);

        return entity;
    }

    private String generatePassword () {
        String newPassword = UUID.randomUUID().toString();
        return newPassword.substring(0,8);
    }

    public  Optional<SysUserEntity> findByEmailAndActiveIsTrue(String email) {
        return systemUserRepository.findByEmailAndActiveIsTrue(email);
    }

    public void forget (String email) {
        SysUserEntity entity = systemUserRepository.findByEmailAndActiveIsTrue(email)
                .orElseThrow(GenericNotFoundException::new);

        String newPassword = generatePassword();
        entity.setPassword(passwordEncoder.encode(newPassword));
        entity.setChangePassword(true);

        entity = systemUserRepository.save(entity);

        asyncService.sendNewPassword(entity, newPassword);
    }

    public  SysUserEntity partialUpdate(UUID userId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        SysUserEntity entity = systemUserRepository.findByIdAndActiveIsTrue(userId)
                .orElseThrow(GenericNotFoundException::new);

        entity = applyPatch(patch, entity);
        entity = systemUserRepository.save(entity);

        return entity;
    }

    private SysUserEntity applyPatch(JsonPatch patch, SysUserEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, SysUserEntity.class);
    }

    public  Page<SysUserEntity> get(Pageable pageable, SysUserFilterRequest filter) {
        Specification<SysUserEntity> filters = specification.filter(filter);

        return systemUserRepository.findAll(filters, pageable);
    }

    public SysUserEntity findById(UUID userId) {
        return systemUserRepository.findById(userId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public SysUserEntity findByIdAndActiveIsTrue(UUID userId) {
        return systemUserRepository.findByIdAndActiveIsTrue(userId)
                .orElseThrow(GenericNotFoundException::new);
    }

    private void validateUserAccess(SysUserEntity systemUser, SysUserEntity entity) {
        List<UUID> systemUserCompanies = systemUser.getEmployee().getCompanyIds();
        List<UUID> entityCompanies = entity.getEmployee().getCompanyIds();

        Optional<UUID> match = systemUserCompanies.stream()
                .filter(l -> entityCompanies.contains(l)).findFirst();

        if (match.isEmpty())
            throw new GenericForbiddenException();
    }

//    private SysUserResponse setAccessGroupInfo(SysUserResponse response) {
//        if (response.getAccessGroupIds() != null) {
//            response.setAccessGroups(response.getAccessGroupIds().stream()
//                    .map(a -> accessGroupService.getById(a))
//                    .collect(Collectors.toList()));
//        }
//        return response;
//    }

    public void delete(UUID userId) {
        SysUserEntity entity = systemUserRepository.findById(userId)
                .orElseThrow(GenericNotFoundException::new);
        systemUserRepository.delete(entity);
    }
}
