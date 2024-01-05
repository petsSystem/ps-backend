package br.com.petshop.system.user.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericIncorrectPasswordException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.company.service.CompanyService;
import br.com.petshop.system.profile.model.dto.response.ProfileResponse;
import br.com.petshop.system.profile.service.ProfileValidateService;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import br.com.petshop.system.user.repository.SysUserRepository;
import br.com.petshop.system.user.repository.SysUserSpecification;
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
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ProfileValidateService profileService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SysUserAsyncService asyncService;

    public SysUserEntity create(SysUserEntity request) {
        Optional<SysUserEntity> entity = repository.findByCpf(request.getCpf());

        if (entity.isPresent())
            throw new GenericAlreadyRegisteredException();

        //verifica se companies existem e estao ativas
        checkCompanyIds(request);

        List<ProfileResponse> profiles = getProfiles(request);
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

    public List<ProfileResponse> getProfiles(SysUserEntity request) {
        return request.getProfileIds().stream()
                .map(p -> profileService.getById(p))
                .collect(Collectors.toList());
    }

    private String generatePassword() {
        String newPassword = UUID.randomUUID().toString();
        return newPassword.substring(0,8);
    }

    private Role getRole(List<ProfileResponse> profiles) {
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

    private List<UUID> getProfileIds(List<ProfileResponse> profiles) {
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
        entity.setChangePassword(false);
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

    public SysUserEntity updateCurrentCompany(SysUserEntity user, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        SysUserEntity entity = repository.findById(user.getId())
                .orElseThrow(GenericNotFoundException::new);

        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        String currentCompanyValue = ((ObjectNode) patched).get("currentCompanyId").asText();

        entity.setCurrentCompanyId(UUID.fromString(currentCompanyValue));

        return repository.save(entity);
    }

    public SysUserEntity updateById(SysUserEntity request, SysUserEntity entity) {
        if (request.getCompanyIds() != null && !request.getCompanyIds().isEmpty())
            for (UUID companyId : request.getCompanyIds())
                if (!entity.getCompanyIds().contains(companyId)) {
                    entity.getCompanyIds().add(companyId);
                    request.setCompanyIds(null);
                }

        if (request.getProfileIds() != null && !request.getProfileIds().isEmpty())
            for (UUID profileId : request.getProfileIds())
                if (!entity.getProfileIds().contains(profileId)) {
                    entity.getProfileIds().add(profileId);
                    request.setProfileIds(null);
                }

        entity = convert.updateRequestIntoEntity(request, entity);

        return repository.save(entity);
    }

    public Page<SysUserEntity> findAllByCompanyId(UUID companyId, Pageable pageable) {
        Specification<SysUserEntity> filters = specification.filter(companyId);
        return repository.findAll(filters, pageable);
    }

    public SysUserEntity findById(UUID userId) {
        return repository.findById(userId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public CompanyEntity getCompanyInfo(SysUserEntity entity) {
        if (entity.getCurrentCompanyId() == null)
            return getFirstCompany(entity);
        else
            return getCurrentCompany(entity);
    }

    public CompanyEntity getFirstCompany(SysUserEntity entity) {
        for(UUID companyId : entity.getCompanyIds()) {
            CompanyEntity company = companyService.findById(companyId);
            if (company.getActive())
                return company;
        }
        return new CompanyEntity();
    }

    public CompanyEntity getCurrentCompany(SysUserEntity entity) {
        UUID companyId = entity.getCurrentCompanyId();
        return companyService.findById(companyId);
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
