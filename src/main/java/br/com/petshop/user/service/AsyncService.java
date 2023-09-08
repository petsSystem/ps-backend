package br.com.petshop.user.service;

import br.com.petshop.dao.entity.AppUserEntity;
import br.com.petshop.notification.MailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AsyncService {
    @Autowired private AppUserService appUserService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private MailNotificationService mailNotificationService;

    @Async
    public void forget(AppUserEntity userEntity) {
        String newPassword = generatePassword();
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userEntity.setChangePassword(true);

        appUserService.save(userEntity);

        mailNotificationService.send(userEntity.getEmail(), newPassword);
    }

    private String generatePassword() {
        String newPassword = UUID.randomUUID().toString();
        return newPassword.substring(0,6);
    }


}
