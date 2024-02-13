package br.com.petshop.product.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.commons.service.ValidationCommonService;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProductValidateService extends ValidationCommonService {
    public Boolean getActiveCategories(Principal authentication) {
        Role role = getSysRole(authentication);
        if (role == Role.ADMIN)
            return false;

        return true;
    }
}
