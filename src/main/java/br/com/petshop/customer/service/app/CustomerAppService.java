package br.com.petshop.customer.service.app;

import br.com.petshop.commons.exception.EmailTokenException;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericIncorrectPasswordException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.repository.CustomerRepository;
import br.com.petshop.customer.repository.CustomerSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CustomerAppService {
    private Logger log = LoggerFactory.getLogger(CustomerAppService.class);
    @Autowired private CustomerRepository repository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CustomerSpecification specification;

    public CustomerEntity create(CustomerEntity request) {

        CustomerEntity entity = repository.findByCpfAndActiveIsTrue(request.getCpf()).orElse(null);

        if (entity != null) {
            if (entity.getAppStatus() == AppStatus.ACTIVE)
                throw new GenericAlreadyRegisteredException();
            else {
                entity.setAppStatus(AppStatus.ACTIVE);
                //enviar email com validaçao de email
                return repository.save(entity);
            }
        }

        entity.setAppStatus(AppStatus.ACTIVE);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setEmailToken(generateToken());
        entity.setEmailTokenTime(LocalDateTime.now());
        entity.setUsername(entity.getCpf());

        return repository.save(entity);
    }

    public CustomerEntity findByCpfAndActiveIsTrue(String username) {
        return repository.findByCpfAndActiveIsTrue(username)
                .orElseThrow(GenericNotFoundException::new);
    }

    public CustomerEntity findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(GenericNotFoundException::new);
    }

    public CustomerEntity forget(CustomerEntity entity) {
        String newPassword = generateToken();
        entity.setPassword(passwordEncoder.encode(newPassword));
        entity.setChangePassword(true);

        repository.save(entity);

        entity.setPassword(newPassword);
        return entity;
    }

    private String generateToken() {
        int number = (int) (100000 + Math.random() * (999999 - 100000 + 1));
        return String.valueOf(number);
    }

    public CustomerEntity updateById(CustomerEntity entity) {
        return repository.save(entity);
    }

    public CustomerEntity changePassword(CustomerEntity entity, CustomerChangePasswordRequest request) {
        if (!(BCrypt.checkpw(request.getOldPassword(), entity.getPassword())))
            throw new GenericIncorrectPasswordException();

        entity.setPassword(passwordEncoder.encode(request.getNewPassword()));
        entity.setChangePassword(false);

        return repository.save(entity);
    }

    public CustomerEntity associateCompanyId (CustomerEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {


        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        String companyIdString = ((ObjectNode) patched).get("companyIds").get(0).toString();
        UUID companyId = UUID.fromString(companyIdString.replaceAll("\"", ""));

        if (!entity.getFavorites().contains(companyId))
            entity.getFavorites().add(companyId);

        if (!entity.getCompanyIds().contains(companyId))
            entity.getCompanyIds().add(companyId);

        return repository.save(entity);
    }

    public CustomerEntity desassociateCompanyId (CustomerEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        String companyIdString = ((ObjectNode) patched).get("companyIds").get(0).toString();
        UUID companyId = UUID.fromString(companyIdString.replaceAll("\"", ""));

        entity.getFavorites().remove(companyId);

        return repository.save(entity);
    }

    public CustomerEntity emailValidate(CustomerEntity entity, String token) {
        if (token.equalsIgnoreCase(entity.getEmailToken()) &&
                !emailTokenExpired(entity.getEmailTokenTime())) {
            entity.setEmailValidated(true);

            return repository.save(entity);

        } else
            throw new EmailTokenException();
    }

    private Boolean emailTokenExpired(LocalDateTime emailTokenTime) {
        if (LocalDateTime.now().isAfter(emailTokenTime.plusMinutes(1)))
            throw new EmailTokenException();
        return false;
    }

    public void deactivate(CustomerEntity entity){
        entity.setActive(false);
        repository.save(entity);
    }
}
