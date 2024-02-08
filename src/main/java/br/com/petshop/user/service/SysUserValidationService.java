package br.com.petshop.user.service;

import br.com.petshop.commons.exception.GenericForbiddenException;
import br.com.petshop.commons.service.ValidationCommonService;
import br.com.petshop.user.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class SysUserValidationService extends ValidationCommonService {

    public void activate(Principal authentication, UserEntity entity) {
        UserEntity user = getSysAuthUser(authentication);

        if (user.getId().equals(entity.getId()))
            throw new GenericForbiddenException();
    }
}
