package br.com.petshop.user.service;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericIncorrectPasswordException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.user.model.dto.request.SysUserPasswordRequest;
import br.com.petshop.user.model.entity.UserEntity;
import br.com.petshop.user.repository.UserRepository;
import br.com.petshop.user.repository.UserSpecification;
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

import java.util.Optional;
import java.util.UUID;

/**
 * Classe responsável pelos serviços de usuários do sistema web.
 */
@Service
public class SysUserService {
    private Logger log = LoggerFactory.getLogger(SysUserService.class);
    @Autowired private UserRepository repository;
    @Autowired private UserSpecification specification;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * Método de envio de email com nova senha, no caso de esquecimento de senha.
     * @param username - cpf do usuário do sistema web.
     * @return - entidade usuário.
     */
    public UserEntity forget (String username) {
        UserEntity entity = repository.findByUsernameAndActiveIsTrue(username)
                .orElseThrow(GenericNotFoundException::new);

        String newPassword = generateToken();
        entity.setPassword(passwordEncoder.encode(newPassword));
        entity.setChangePassword(true);

        entity = repository.save(entity);
        entity.setPassword(newPassword);

        return entity;
    }

    /**
     * Método de criação de usuário do sistema web.
     * @param request - dados de criação do usuário no formato de entidade.
     * @return - entidade usuário.
     */
    public UserEntity create(UserEntity request) {
        Optional<UserEntity> entity = repository.findByCpfAndActiveIsTrue(request.getCpf());

        //caso já exista no sistema, substituir as lojas para o novo cadastro
        if (entity.isPresent()) {
            UserEntity user = entity.get();
            user.setCompanyIds(request.getCompanyIds());
            repository.save(request);
            throw new GenericAlreadyRegisteredException();
        }

        String password = generateToken();

        request.setUsername(request.getCpf());
        request.setPassword(passwordEncoder.encode(password));

        UserEntity user = repository.save(request);
        user.setPassword(password);

        return user;
    }

    /**
     * Método que gera token.
     * @return - token.
     */
    private String generateToken() {
        int number = (int) (100000 + Math.random() * (999999 - 100000 + 1));
        return String.valueOf(number);
    }

    /**
     * Método de busca de usuário ativo através da informação do id.
     * @param userId - id do cadastro do usuário.
     * @return - entidade usuário.
     */
    public UserEntity findByIdAndActiveIsTrue(UUID userId) {
        return repository.findByIdAndActiveIsTrue(userId)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método de atualização de usuário do sistema web.
     * @param entity - dados de atualização do usuário no formato de entidade.
     * @return - entidade usuário.
     */
    public UserEntity updateById(UserEntity entity) {
        return repository.save(entity);
    }

    /**
     * Método de ativação/desativação de usuário do sistema web.
     * @param entity - entidade usuário.
     * @param patch - dados de atualização do status do usuário.
     * @return - entidade usuário.
     * @throws JsonPatchException
     * @throws JsonProcessingException
     */
    public UserEntity activate(UserEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        Boolean activeValue = ((ObjectNode) patched).get("active").asBoolean();

        entity.setActive(activeValue);

        return repository.save(entity);
    }

    /**
     * Método de recuperação de usuários de acordo com filtro informado.
     * @param companyId - id do cadastro da loja/petshop.
     * @param pageable - dados de paginação.
     * @return - lista de entidades usuários.
     */
    public Page<UserEntity> findAllByFilter(UUID companyId, Pageable pageable) {
        Specification<UserEntity> filters = specification.filter(companyId);
        return repository.findAll(filters, pageable);
    }

    /**
     * Método de recuperação de usuário, através da informação de id.
     * @param userId - id do cadastro do usuário.
     * @return - entidade usuário.
     */
    public UserEntity findById(UUID userId) {
        return repository.findById(userId)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método de troca de senha do usuário logado no sistema web.
     * @param userId - id do cadastro do usuário.
     * @param request - dados da senha a ser alterada.
     * @return - entidade usuário.
     */
    public UserEntity changePassword(UUID userId, SysUserPasswordRequest request) {
        UserEntity entity = repository.findByIdAndActiveIsTrue(userId)
                .orElseThrow(GenericNotFoundException::new);

        if (!(BCrypt.checkpw(request.getOldPassword(), entity.getPassword())))
            throw new GenericIncorrectPasswordException();

        entity.setPassword(passwordEncoder.encode(request.getNewPassword()));
        entity.setChangePassword(false);

        return repository.save(entity);
    }

    /**
     *
     * @param user - entidade usuário.
     * @param patch - informação da última loja/petshop acessado pelo usuário do sistema web.
     * @return - entidade usuário.
     * @throws JsonPatchException
     * @throws JsonProcessingException
     */
    public UserEntity updateCurrentCompany(UserEntity user, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        UserEntity entity = repository.findById(user.getId())
                .orElseThrow(GenericNotFoundException::new);

        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        String currentCompanyValue = ((ObjectNode) patched).get("currentCompanyId").asText();

        entity.setCurrentCompanyId(UUID.fromString(currentCompanyValue));

        return repository.save(entity);
    }
}
