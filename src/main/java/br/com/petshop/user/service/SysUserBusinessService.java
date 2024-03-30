package br.com.petshop.user.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericForbiddenException;
import br.com.petshop.commons.exception.GenericIncorrectPasswordException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.exception.GenericParamMissingException;
import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.company.model.dto.response.CompanyResponse;
import br.com.petshop.company.service.CompanyBusinessService;
import br.com.petshop.notification.MailNotificationService;
import br.com.petshop.notification.MailType;
import br.com.petshop.product.model.entity.ProductEntity;
import br.com.petshop.profile.model.dto.Permission;
import br.com.petshop.profile.model.dto.response.ProfileResponse;
import br.com.petshop.profile.service.ProfileBusinessService;
import br.com.petshop.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.schedule.service.ScheduleBusinessService;
import br.com.petshop.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.user.model.dto.request.SysUserPasswordRequest;
import br.com.petshop.user.model.dto.request.SysUserUpdateProfileRequest;
import br.com.petshop.user.model.dto.request.SysUserUpdateRequest;
import br.com.petshop.user.model.dto.response.SysUserMeResponse;
import br.com.petshop.user.model.dto.response.SysUserProfileResponse;
import br.com.petshop.user.model.dto.response.SysUserResponse;
import br.com.petshop.user.model.dto.response.SysUserTableResponse;
import br.com.petshop.user.model.entity.UserEntity;
import br.com.petshop.user.model.enums.Message;
import com.github.fge.jsonpatch.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe responsável pelas regras de negócio do usuário do sistema web.
 */
@Service
public class SysUserBusinessService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(SysUserBusinessService.class);
    @Autowired private SysUserValidationService validate;
    @Autowired private SysUserService service;
    @Autowired private SysUserConverterService converter;
    @Autowired private ProfileBusinessService profileService;
    @Autowired private MailNotificationService mailService;
    @Autowired private CompanyBusinessService companyService;
    @Autowired private ScheduleBusinessService scheduleService;

    /**
     * Método de envio de email com nova senha, no caso de esquecimento de senha.
     * @param username - cpf do usuário do sistema web.
     */
    public void forget (String username) {
        try {
            username = username.replaceAll("[^0-9]", "");
            //chama serviço de esquecimento de senha
            UserEntity entity = service.forget(username);

            mailService.sendEmail(entity.getName(), entity.getEmail(), entity.getPassword(), MailType.NEW_PASSWORD);

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_SENDING_PASSWORD_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_SENDING_PASSWORD_ERROR.get(), ex);
        }
    }

    /**
     * Método de criação de usuário do sistema web.
     * @param authentication - dados do usuário logado.
     * @param request - dto contendo dados de criação do usuário.
     * @return - dados do usuário do sistema web.
     */
    public SysUserResponse create(Principal authentication, SysUserCreateRequest request) {
        try {
            //valida acesso da loja
            validate.accessByCompany(authentication, request.getCompanyIds());

            if (request.getProfileIds() == null || request.getProfileIds().isEmpty())
                throw new GenericParamMissingException();

            //converte request em entidade
            UserEntity entity = converter.createRequestIntoEntity(request);

            //verifica a role comum de todos os perfis salvos
            Role role = profileService.getCommonRole(entity.getProfileIds());
            entity.setRole(role);

            //cria a entidade user
            entity = service.create(entity);

            //envia email com senha para o novo usuario
            mailService.sendEmail(entity.getName(), entity.getEmail(), entity.getPassword(), MailType.NEW_PASSWORD);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.USER_FORBIDDEN_ERROR.get(), ex);
        } catch (GenericAlreadyRegisteredException ex) {
            log.error(Message.USER_ALREADY_REGISTERED_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.USER_ALREADY_REGISTERED_ERROR.get(), ex);
        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_COMPANY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, Message.USER_COMPANY_NOT_FOUND_ERROR.get(), ex);
        } catch (GenericParamMissingException ex) {
            log.error(Message.USER_PROFILE_PARAM_MISSING_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, Message.USER_PROFILE_PARAM_MISSING_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_CREATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_CREATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de atualização de usuário do sistema web.
     * @param authentication - dados do usuário logado.
     * @param userId - id de cadastro do usuário.
     * @param request - dto contendo dados de atualização do usuário.
     * @return - dados do usuário do sistema web.
     */
    public SysUserResponse updateById(Principal authentication, UUID userId, SysUserUpdateRequest request) {
        try {
            //recupera o usuario ativo pelo id
            UserEntity entity = service.findByIdAndActiveIsTrue(userId);

            if (request.getProfileIds() == null || request.getProfileIds().isEmpty())
                throw new GenericParamMissingException();

            //valida acesso do usuário e loja
            validate.accessByUser(authentication, entity);

            //converte request em entidade
            UserEntity entityRequest = converter.updateRequestIntoEntity(request);
            entity = converter.updateRequestIntoEntity(entityRequest, entity);

            //faz atualizaçao da entidade
            entity = service.updateById(entity);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND_ERROR.get(), ex);
        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.USER_FORBIDDEN_ERROR.get(), ex);
        } catch (GenericParamMissingException ex) {
            log.error(Message.USER_PROFILE_PARAM_MISSING_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, Message.USER_PROFILE_PARAM_MISSING_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_UPDATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de ativação/desativação de usuário do sistema web.
     * @param authentication - dados do usuário logado.
     * @param userId - id do cadastro do usuário.
     * @param patch - dados de alteração do pet.
     * @return - dados do usuário do sistema web.
     */
    public SysUserResponse activate(Principal authentication, UUID userId, JsonPatch patch) {
        try {
            //recupera o usuario pelo id
            UserEntity entity = service.findById(userId);

            //valida acesso a loja
            validate.accessByCompany(authentication, entity.getCompanyIds());

            //valida se é o proprio usuario quem esta agindo
            validate.activate(authentication, entity);

            //ativa/desativa usuario
            entity = service.activate(entity, patch);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.USER_FORBIDDEN_ERROR.get(), ex);
        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ACTIVATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ACTIVATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de recuperação de dados de usuários do sistema web, através da infomração do companyId.
     * @param authentication - dados do usuário logado.
     * @param companyId - id do cadastro da loja/petshop.
     * @param productId - id do cadastro do produto/serviço.
     * @param pageable - dados de paginação.
     * @return - Lista de usuários do sistma web.
     */
    public  Page<SysUserTableResponse> get(Principal authentication, UUID companyId, UUID productId, Pageable pageable) {
        try {
            //valida acesso a loja
            validate.accessByCompany(authentication, companyId);

            //find by filter (companyId + productId (sendo esse ultimo opcional))
            Page<UserEntity> entities = service
                    .findAllByFilter(companyId, pageable);

            //se productId informado, fluxo de agendamento. Verificar se usuário tem agenda para o produto
            if (productId != null)
                entities = checkSchedule(authentication, companyId, productId, entities);

            //converte entidade para a resposta
            List<SysUserTableResponse> response = entities.stream()
                    .map(c -> converter.entityIntoTableResponse(c))
                    .collect(Collectors.toList());

            //ordena por status + nome
            Collections.sort(response, Comparator.comparing(SysUserTableResponse::getActive).reversed()
                    .thenComparing(SysUserTableResponse::getName));

            //pagina a lista
            return new PageImpl<>(response);

        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.USER_FORBIDDEN_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método que verifica se há agenda para um determinado usuário x produto do sistema web.
     * Esse método será utilizado para o fluxo de agendamneto.
     * @param authentication - dados do usuário logado
     * @param companyId - id do cadastro da loja/petshop
     * @param productId - id do cadastro do produto/serviço
     * @param entities - lista dos dados do usuário
     * @return - lista dos dados do usuário
     */
    private Page<UserEntity> checkSchedule(Principal authentication, UUID companyId, UUID productId, Page<UserEntity> entities) {
        List<UserEntity> entitiesFiltered = new ArrayList<>();

        ScheduleFilterRequest filter = ScheduleFilterRequest.builder()
                .companyId(companyId)
                .productId(productId)
                .build();

        entities.forEach(e -> {
            filter.setUserId(e.getId());
            List<ScheduleResponse> schedules = scheduleService
                    .getByFilter(authentication, filter);

            if (!schedules.isEmpty())
                entitiesFiltered.add(e);
        });

        return new PageImpl<>(entitiesFiltered);
    }

    /**
     * Método de recuperação de dados do usuário do sistema web, através da informação do id.
     * @param authentication - dados do usuário logado.
     * @param userId - id do cadastro do usuário.
     * @return - dados do usuário do sistema web.
     */
    public SysUserResponse getById(Principal authentication, UUID userId) {
        try {
            //recupera entidade pelo userId
            UserEntity entity = service.findById(userId);

            //valida acesso por usuario e loja
            validate.accessByUser(authentication, entity);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND_ERROR.get(), ex);
        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.USER_FORBIDDEN_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método de recuperação de dados do usuário logado no sistema web.
     * @param authentication - dados do usuário logado.
     * @return - dados do usuário do sistema web.
     */
    public SysUserMeResponse getMeInfo(Principal authentication) {
        try {
            //recupera o id do usuário logado
            UserEntity entity = getSysAuthUser(authentication);

            //recupera a entidade do usuário logado
            entity = service.findById(entity.getId());

            //recupera as informações da ultima loja acessada do usuário logado
            //(se nao tiver, carrega a primeira da lista do usuario logado)
            CompanyResponse companyEntity = companyService
                    .findActiveCompany(entity.getCurrentCompanyId(), entity.getCompanyIds());

            //carrega as permissoes
            List<ProfileResponse> profiles = profileService.getByIds(entity.getProfileIds());
            List<Permission> permissions = profiles.stream()
                    .flatMap(p -> p.getPermissions().stream())
                    .collect(Collectors.toList());

            //converte a entidade na resposta final
            SysUserMeResponse response = converter.entityIntoMeResponse(entity);
            response.setPermissions(permissions);
            response.setCompanyId(companyEntity.getId());
            response.setCompanyName(companyEntity.getName());

            return response;

        } catch (Exception ex) {
            log.error(Message.USER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método de recuperação de dados do usuário logado no sistema web.
     * @param authentication - dados do usuário logado.
     * @return - dados do usuário do sistema web.
     */
    public SysUserProfileResponse getProfile(Principal authentication) {
        try {
            //recupera o usuario logado
            UserEntity entity = getSysAuthUser(authentication);

            //recupera a entidade do usuario logado
            entity = service.findById(entity.getId());

            //converte a entidade na resposta final
            return converter.entityIntoProfileResponse(entity);

        } catch (Exception ex) {
            log.error(Message.USER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método de atualização de dados do usuário logado no sistema web.
     * @param authentication - dados do usuário logado.
     * @param request - dto contendo dados de atualização do usuário.
     * @return - dados do usuário do sistema web.
     */
    public SysUserProfileResponse updateProfile(Principal authentication, SysUserUpdateProfileRequest request) {
        try {
            //recupera o usuario logado
            UserEntity entity = getSysAuthUser(authentication);

            //recupera a entidade do usuario logado
            entity = service.findById(entity.getId());

            //converte request em entidade
            UserEntity entityRequest = converter.updateProfileRequestIntoEntity(request);
            entity = converter.updateProfileRequestIntoEntity(entityRequest, entity);

            //faz atualizaçao da entidade
            entity = service.updateById(entity);

            //converte a entidade na resposta final
            return converter.entityIntoProfileResponse(entity);

        } catch (Exception ex) {
            log.error(Message.USER_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_UPDATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de troca de senha do usuário logado no sistema web.
     * @param authentication - dados do usuário logado.
     * @param request - dados da senha a ser alterada.
     * @return - dados do usuário do sistema web.
     */
    public SysUserResponse changePassword(Principal authentication, SysUserPasswordRequest request) {
        try {
            //recupera o usuario logado
            UserEntity user = getSysAuthUser(authentication);

            //troca a senha do usuario
            UserEntity entity = service.changePassword(user.getId(), request);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException | GenericIncorrectPasswordException ex) {
            log.error(Message.USER_OLD_PASSWORD_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_OLD_PASSWORD_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_CHANGING_PASSWORD_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_CHANGING_PASSWORD_ERROR.get(), ex);
        }
    }

    /**
     * Método que registra última loja/petshop acessado pelo usuário logado no sistema web.
     * @param authentication - dados do usuário logado.
     * @param patch - dados de alteração da última loja/petshop acessado.
     * @return - dados do usuário do sistema web.
     */
    public SysUserMeResponse updateCurrentCompany(Principal authentication, JsonPatch patch) {
        try {
            //recupera o usuario logado
            UserEntity user = getSysAuthUser(authentication);

            //atualiza a última loja acessada
            UserEntity entity = service.updateCurrentCompany(user, patch);

            //recupera as informações da ultima loja acessada do usuário logado
            CompanyResponse companyEntity = companyService
                    .findActiveCompany(entity.getCurrentCompanyId(), entity.getCompanyIds());

            //carrega as permissoes
            List<ProfileResponse> profiles = profileService.getByIds(entity.getProfileIds());
            List<Permission> permissions = profiles.stream()
                    .flatMap(p -> p.getPermissions().stream())
                    .collect(Collectors.toList());

            //converte a entidade na resposta final
            SysUserMeResponse response = converter.entityIntoMeResponse(entity);
            response.setPermissions(permissions);
            response.setCompanyId(companyEntity.getId());
            response.setCompanyName(companyEntity.getName());

            return response;

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ACTIVATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ACTIVATE_ERROR.get(), ex);
        }
    }
}
