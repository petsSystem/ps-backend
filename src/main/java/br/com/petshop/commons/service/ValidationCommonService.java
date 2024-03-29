package br.com.petshop.commons.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.commons.exception.GenericForbiddenException;
import br.com.petshop.user.model.entity.UserEntity;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * Validações comuns a todo sistema.
 */
public class ValidationCommonService extends AuthenticationCommonService {

    public void accessByCompany(Principal authentication, List<UUID> companyIds) {
        companyIds.stream()
                .forEach(c -> accessByCompany(authentication, c));
    }

    public void accessByCompany(Principal authentication, UUID companyId) {
        UserEntity user = getSysAuthUser(authentication);

        if (user.getRole() == Role.ADMIN) return;

        if (!user.getCompanyIds().contains(companyId))
            throw new GenericForbiddenException();
    }

    public void accessByUser(Principal authentication, UserEntity entity) {
        UserEntity user = getSysAuthUser(authentication);

        if (entity.getProfileIds() == null || entity.getProfileIds().isEmpty())
            throw new RuntimeException();

        if (user.getRole() == Role.ADMIN) return;

        accessByCompany(authentication, entity.getCompanyIds());

        if (user.getRole() == Role.USER && (!user.getId().equals(entity.getId())))
            throw new GenericForbiddenException();
    }
}
