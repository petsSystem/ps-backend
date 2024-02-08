package br.com.petshop.customer.service;

import br.com.petshop.customer.model.dto.request.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.dto.request.CustomerSysUpdateRequest;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.model.enums.Origin;
import br.com.petshop.customer.repository.CustomerRepository;
import br.com.petshop.customer.repository.CustomerSpecification;
import br.com.petshop.commons.exception.EmailTokenException;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericIncorrectPasswordException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.notification.MailNotificationService;
import br.com.petshop.notification.MailType;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class
CustomerService {
    private Logger log = LoggerFactory.getLogger(CustomerService.class);
    @Autowired private CustomerRepository repository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MailNotificationService mailService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CustomerConverterService convert;
    @Autowired private CustomerSpecification specification;

    public void forget(String username) {
        CustomerEntity customerEntity = repository.findByUsernameAndActiveIsTrue(username)
                .orElseThrow(GenericNotFoundException::new);

        String newPassword = generateToken();
        customerEntity.setPassword(passwordEncoder.encode(newPassword));
        customerEntity.setChangePassword(true);

        repository.save(customerEntity);

        mailService.sendEmail(customerEntity.getName(), customerEntity.getEmail(), newPassword, MailType.NEW_PASSWORD);
    }

    public CustomerEntity create(CustomerEntity entity) {

        CustomerEntity customerEntity = repository.findByCpf(entity.getCpf()).orElse(null);

        if (customerEntity != null)
            if (customerEntity.getAppStatus() == AppStatus.ACTIVE)
                throw new GenericAlreadyRegisteredException();
            else {
                customerEntity.setAppStatus(AppStatus.ACTIVE);
                customerEntity.setPassword(passwordEncoder.encode(entity.getPassword()));
                sendValidationEmailToken(customerEntity);
                return repository.save(customerEntity);
            }

        if (entity.getOrigin() == Origin.APP) {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            sendValidationEmailToken(entity);
        } else {
            mailService.sendEmail(entity.getName(), entity.getEmail(), "", MailType.APP_INVITATION);
        }

        entity.setUsername(entity.getCpf());

        return repository.save(entity);
    }

    public CustomerEntity associateCompanyId (UUID customerId, JsonPatch patch, Boolean favorite) throws JsonPatchException, JsonProcessingException {
        CustomerEntity entity = findById(customerId);

        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        String companyIdString = ((ObjectNode) patched).get("companyIds").get(0).toString();
        UUID companyId = UUID.fromString(companyIdString.replaceAll("\"", ""));

        if (!entity.getFavorites().contains(companyId))
            entity.getFavorites().add(companyId);

        if (!entity.getCompanyIds().contains(companyId))
            entity.getCompanyIds().add(companyId);

        return repository.save(entity);
    }

    public CustomerEntity desassociateCompanyId (UUID customerId, JsonPatch patch, Boolean favorite) throws JsonPatchException, JsonProcessingException {
        CustomerEntity entity = findById(customerId);

        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        String companyIdString = ((ObjectNode) patched).get("companyIds").get(0).toString();
        UUID companyId = UUID.fromString(companyIdString.replaceAll("\"", ""));

        entity.getFavorites().remove(companyId);

        return repository.save(entity);
    }

    public CustomerEntity update(UUID customerId, CustomerSysUpdateRequest request) {
        CustomerEntity entity = findById(customerId);

        CustomerEntity newEntity = convert.updateRequestIntoEntity(request, entity);

        return repository.save(newEntity);
    }

    public CustomerEntity findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(GenericNotFoundException::new);
    }

    private void sendValidationEmailToken(CustomerEntity userEntity) {
        userEntity.setEmailToken(generateToken());
        userEntity.setEmailTokenTime(LocalDateTime.now());

        mailService.sendEmail(userEntity.getName(), userEntity.getEmail(), userEntity.getEmailToken(), MailType.VALIDATE_EMAIL);
    }

    public CustomerEntity changePassword(CustomerEntity authUser, CustomerChangePasswordRequest request) {
        CustomerEntity entity = repository.findByCpf(authUser.getCpf())
                .orElseThrow(GenericNotFoundException::new);

        if (!(BCrypt.checkpw(request.getOldPassword(), entity.getPassword())))
            throw new GenericIncorrectPasswordException();

        entity.setPassword(passwordEncoder.encode(request.getNewPassword()));
        entity.setChangePassword(false);

        return repository.save(entity);
    }

    private String generateToken() {
        int number = (int) (100000 + Math.random() * (999999 - 100000 + 1));
        return String.valueOf(number);
    }

    public CustomerEntity emailValidate(String cpf, String token) {
        CustomerEntity entity = repository.findByCpf(cpf)
                .orElseThrow(GenericNotFoundException::new);

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

    public void resendEmailValidate(String cpf) {
        CustomerEntity userEntity = repository.findByCpf(cpf)
                .orElseThrow(GenericNotFoundException::new);

        sendValidationEmailToken(userEntity);
    }

    public void deactivate(UUID customerId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        CustomerEntity entity = findById(customerId);
        entity.setActive(false);
        repository.save(entity);
    }

    public Page<CustomerEntity> findAllByCompanyId(UUID companyId, Pageable pageable) {
        Specification<CustomerEntity> filters = specification.filter(companyId);
        return repository.findAll(filters, pageable);
    }
}
