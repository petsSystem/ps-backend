package br.com.petshop.customer.service.app;

import br.com.petshop.commons.exception.EmailTokenException;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericIncorrectPasswordException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.repository.CustomerRepository;
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

/**
 * Classe responsável pelos serviços de clientes do aplicativo mobile.
 */
@Service
public class CustomerAppService {
    private Logger log = LoggerFactory.getLogger(CustomerAppService.class);
    @Autowired private CustomerRepository repository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * Método de criação de cliente.
     * @param request - entidade cliente
     * @return - entidade cliente
     */
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

    /**
     * Método que recupera um cliente ativo pelo cpf.
     * @param username - cpf do cliente
     * @return - entidade cliente
     */
    public CustomerEntity findByCpfAndActiveIsTrue(String username) {
        return repository.findByCpfAndActiveIsTrue(username)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método que recupera um cliente ativo pelo id.
     * @param id - id de cadastro do cliente
     * @return - entidade cliente
     */
    public CustomerEntity findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método que envia email com nova senha, caso cliente do aplicativo mobile esqueça a senha.
     * @param entity - entidade cliente
     * @return - entidade cliente
     */
    public CustomerEntity forget(CustomerEntity entity) {
        String newPassword = generateToken();
        entity.setPassword(passwordEncoder.encode(newPassword));
        entity.setChangePassword(true);

        repository.save(entity);

        entity.setPassword(newPassword);
        return entity;
    }

    /**
     * Método que gera uma senha/token
     * @return - senha/token que contém 6 dígitos compostos por númertos de 100000 a 999999
     */
    private String generateToken() {
        int number = (int) (100000 + Math.random() * (999999 - 100000 + 1));
        return String.valueOf(number);
    }

    /**
     * Método que atualiza os dados do cliente no aplicativo mobile.
     * @param entity - entidade cliente
     * @return - entidade cliente
     */
    public CustomerEntity updateById(CustomerEntity entity) {
        return repository.save(entity);
    }

    /**
     * Método de troca de senha do cliente no aplicativo mobile.
     * @param entity - entidade cliente
     * @param request - dto que contém dados da troca de senha do cliente
     * @return - entidade cliente
     */
    public CustomerEntity changePassword(CustomerEntity entity, CustomerChangePasswordRequest request) {
        if (!(BCrypt.checkpw(request.getOldPassword(), entity.getPassword())))
            throw new GenericIncorrectPasswordException();

        entity.setPassword(passwordEncoder.encode(request.getNewPassword()));
        entity.setChangePassword(false);

        return repository.save(entity);
    }

    /**
     * Método que associa ou favorita um cliente a loja/petshop no sistema web.
     * @param entity - entidade cliente
     * @param patch - dados de atualização da associação de loja/petshop
     * @return - entidade cliente
     * @throws JsonPatchException
     * @throws JsonProcessingException
     */
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

    /**
     * Método que desfavorita um cliente a loja/petshop no sistema web.
     * @param entity - entidade cliente
     * @param patch - dados de atualização da desassociação de loja/petshop
     * @return - entidade cliente
     * @throws JsonPatchException
     * @throws JsonProcessingException
     */
    public CustomerEntity desassociateCompanyId (CustomerEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        String companyIdString = ((ObjectNode) patched).get("companyIds").get(0).toString();
        UUID companyId = UUID.fromString(companyIdString.replaceAll("\"", ""));

        entity.getFavorites().remove(companyId);

        return repository.save(entity);
    }

    /**
     * Método de validação de token enviado via email, para validação do email do cliente do aplicativo.
     * @param entity - entidade cliente
     * @param token
     * @return - entidade cliente
     */
    public CustomerEntity emailValidate(CustomerEntity entity, String token) {
        if (token.equalsIgnoreCase(entity.getEmailToken()) &&
                !emailTokenExpired(entity.getEmailTokenTime())) {
            entity.setEmailValidated(true);

            return repository.save(entity);

        } else
            throw new EmailTokenException();
    }

    /**
     * Método de verificação de validade do token
     * @param emailTokenTime - data/horário de expiração do token
     * @return - true = se token estiver expirado / false = se token não estiver expirado
     */
    private Boolean emailTokenExpired(LocalDateTime emailTokenTime) {
        if (LocalDateTime.now().isAfter(emailTokenTime.plusMinutes(1)))
            throw new EmailTokenException();
        return false;
    }

    /**
     * Método de desativação de cliente do aplicativo mobile. (exclusão lógica)
     * @param entity - entidade cliente
     */
    public void deactivate(CustomerEntity entity){
        entity.setActive(false);
        repository.save(entity);
    }
}
