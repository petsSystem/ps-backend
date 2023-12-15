package br.com.petshop.system.user.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericIncorrectPasswordException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.company.service.CompanyService;
import br.com.petshop.system.user.model.dto.response.SysUserMeResponse;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import br.com.petshop.system.user.repository.SysUserRepository;
import br.com.petshop.system.user.repository.SysUserSpecification;
import br.com.petshop.system.profile.model.entity.ProfileEntity;
import br.com.petshop.system.profile.service.ProfileService;
import br.com.petshop.system.user.model.dto.request.SysUserPasswordRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SysUserService {
    Logger log = LoggerFactory.getLogger(SysUserService.class);
    @Autowired private SysUserRepository repository;
    @Autowired private SysUserSpecification specification;
    @Autowired private SysUserConverterService convert;
    @Autowired private CompanyService companyService;
    @Autowired private br.com.petshop.system.user.service.SysUserService sysUserService;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ProfileService profileService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SysUserAsyncService asyncService;

    public SysUserEntity create(SysUserEntity request) {
        Optional<SysUserEntity> entity = repository.findByCpf(request.getCpf());

        if (entity.isPresent())
            throw new GenericAlreadyRegisteredException();

        //verifica se companies existem e estao ativas
        checkCompanyIds(request);

        List<ProfileEntity> profiles = getProfiles(request);
        String password = generatePassword();

        request.setUsername(request.getEmail());
        request.setPassword(passwordEncoder.encode(password));
        request.setChangePassword(true);
        request.setRole(getRole(profiles));
        request.setActive(true);
        request.setProfileIds(getProfileIds(profiles));

        SysUserEntity user = repository.save(request);

        asyncService.sendNewPassword(user, password);

        return user;

    }

    private void checkCompanyIds(SysUserEntity request) {
        for(UUID companyId : request.getCompanyIds())
            companyService.findByIdAndActiveIsTrue(companyId);
    }

    public List<ProfileEntity> getProfiles(SysUserEntity request) {
        return request.getProfileIds().stream()
                .map(p -> profileService.findById(p))
                .collect(Collectors.toList());
    }

    private String generatePassword() {
        String newPassword = UUID.randomUUID().toString();
        return newPassword.substring(0,8);
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

    public void forget (String email) {
        SysUserEntity entity = repository.findByUsernameAndActiveIsTrue(email)
                .orElseThrow(GenericNotFoundException::new);

        String newPassword = generatePassword();
        entity.setPassword(passwordEncoder.encode(newPassword));
        entity.setChangePassword(true);

        entity = repository.save(entity);

        asyncService.sendNewPassword(entity, newPassword);
    }

    public SysUserEntity changePassword(UUID userId, SysUserPasswordRequest request) {
        SysUserEntity entity = repository.findByIdAndActiveIsTrue(userId)
                .orElseThrow(GenericNotFoundException::new);

        if (!(BCrypt.checkpw(request.getOldPassword(), entity.getPassword())))
            throw new GenericIncorrectPasswordException();

        entity.setPassword(passwordEncoder.encode(request.getNewPassword()));
        entity = repository.save(entity);

        return entity;
    }

    public SysUserEntity active(UUID userId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        SysUserEntity entity = repository.findById(userId)
                .orElseThrow(GenericNotFoundException::new);

        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        Boolean activeValue = ((ObjectNode) patched).get("active").asBoolean();

        entity.setActive(activeValue);

        return repository.save(entity);
    }

    public SysUserEntity updateById(SysUserEntity request, SysUserEntity entity) {

        entity = convert.updateRequestIntoEntity(request, entity);

        return repository.save(entity);
    }

    public Page<SysUserEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<SysUserEntity> findByCompanyId(SysUserEntity user, Pageable pageable) {
        Page<SysUserEntity> response = new PageImpl<>(new ArrayList<>());

        for(UUID companyId : user.getCompanyIds()) {
            Specification<SysUserEntity> filters = specification.filter(companyId);
            response = repository.findAll(filters, pageable);
        }
        return new PageImpl<>(new ArrayList<>());
    }

    public SysUserEntity findById(UUID userId) {
        return repository.findById(userId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public SysUserEntity findByIdAndActiveIsTrue(UUID userId) {
        return repository.findByIdAndActiveIsTrue(userId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public SysUserEntity save(SysUserEntity entity) {
        return repository.save(entity);
    }

    public SysUserEntity findByIdAndActive(UUID userId) {
        Optional<SysUserEntity> entity = repository.findByIdAndActiveIsTrue(userId);
        if (entity.isEmpty())
            throw new GenericNotFoundException();
        return entity.get();
    }
}
