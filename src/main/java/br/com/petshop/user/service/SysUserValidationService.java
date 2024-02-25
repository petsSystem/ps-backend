package br.com.petshop.user.service;

import br.com.petshop.commons.exception.GenericForbiddenException;
import br.com.petshop.commons.service.ValidationCommonService;
import br.com.petshop.user.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;

/**
 * Classe responsável pelas regras de validação de usuário do sistema web.
 */
@Service
public class SysUserValidationService extends ValidationCommonService {

    /**
     * Regra que impossibilida o usuário habilitar ou desabilitar o seu próprio usuário.
     * @param authentication - dados do usuário logado.
     * @param entity - entidade de usuário
     */
    public void activate(Principal authentication, UserEntity entity) {
        UserEntity user = getSysAuthUser(authentication);

        if (user.getId().equals(entity.getId()))
            throw new GenericForbiddenException();
    }
}
