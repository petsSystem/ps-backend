package br.com.petshop.profile.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.commons.service.ValidationCommonService;
import br.com.petshop.profile.model.entity.ProfileEntity;
import br.com.petshop.user.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileValidationService extends ValidationCommonService {

    public void accessLabels(Principal authentication, List<ProfileEntity> entities) {
        UserEntity user = getSysAuthUser(authentication);

        if (user.getRole() != Role.ADMIN) {
            entities = entities.stream()
                    .filter(l -> !l.getName().equalsIgnoreCase("Administrador") &&
                            !l.getName().equalsIgnoreCase("Proprietário"))
                    .collect(Collectors.toList());
        }
    }
}
