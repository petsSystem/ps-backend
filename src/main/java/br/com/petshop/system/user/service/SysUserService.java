package br.com.petshop.system.user.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.profile.model.entity.ProfileEntity;
import br.com.petshop.system.profile.service.ProfileService;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import br.com.petshop.system.employee.service.EmployeeService;
import br.com.petshop.system.user.model.dto.request.SysUserFilterRequest;
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
    @Autowired private ProfileService profileService;

    public SysUserEntity create (EmployeeEntity employee) {

        //pegar os dados do profile (role e ids) se mais de um, chegar num denominador comum
        List<ProfileEntity> profiles = employee.getProfiles().stream()
                .map(p -> profileService.findByName(p))
                .collect(Collectors.toList());

        Role finalRole = getRole(profiles);
        List<UUID> profileIds = getProfileIds(profiles);
        String password = generatePassword();

        SysUserEntity entity = SysUserEntity.builder()
                .username(employee.getEmail())
                .password(passwordEncoder.encode(password))
                .changePassword(false)
                .role(finalRole)
                .active(true)
                .employee(employee)
                .profileIds(profileIds)
                .build();

        entity = systemUserRepository.save(entity);

        asyncService.sendNewPassword(entity, password);

        return entity;
    }

    public SysUserEntity save (SysUserEntity entity) {
        return systemUserRepository.save(entity);
    }

    private Role getRole(List<ProfileEntity> profiles) {
        List<Role> roles = profiles.stream()
                .map(p -> p.getRole())
                .collect(Collectors.toList());

        Role finalRole = null;
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            if (finalRole == null)
                finalRole = role;

            if (role == Role.ADMIN) {
                finalRole = role;
                break;
            }

            if (role == Role.OWNER && finalRole != Role.ADMIN)
                finalRole = role;

            if (role == Role.MANAGER && finalRole != Role.ADMIN && finalRole != Role.OWNER)
                finalRole = role;
        }

        return finalRole;
    }

    private List<UUID> getProfileIds(List<ProfileEntity> profiles) {
        return profiles.stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
    }

    private String generatePassword () {
        String newPassword = UUID.randomUUID().toString();
        return newPassword.substring(0,8);
    }

    public SysUserEntity activate(UUID userId, Boolean active) {
        Optional<SysUserEntity> entity = systemUserRepository.findById(userId);
        SysUserEntity userEntity = entity.get();
        userEntity.setActive(active);

        return systemUserRepository.save(userEntity);
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

    public  Optional<SysUserEntity> findByUsername(String email) {
        return systemUserRepository.findByUsername(email);
    }

    public  Optional<SysUserEntity> findByUsernameAndActiveIsTrue(String email) {
        return systemUserRepository.findByUsernameAndActiveIsTrue(email);
    }

    public void forget (String email) {
        SysUserEntity entity = systemUserRepository.findByUsernameAndActiveIsTrue(email)
                .orElseThrow(GenericNotFoundException::new);

        String newPassword = generatePassword();
        entity.setPassword(passwordEncoder.encode(newPassword));
        entity.setChangePassword(true);

        entity = systemUserRepository.save(entity);

        asyncService.sendNewPassword(entity, newPassword);
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
