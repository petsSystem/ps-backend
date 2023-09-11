package br.com.petshop.user.service;

import br.com.petshop.dao.entity.AppUserEntity;
import br.com.petshop.notification.MailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AsyncService {
    @Autowired private AppUserService appUserService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private MailNotificationService mailNotificationService;

    @Async
    public void forget(AppUserEntity userEntity, String newPassword) {

        String subject = "Nova senha - APP Pet System";
        String body = "Sua nova senha é: " + newPassword;

        mailNotificationService.send(userEntity.getEmail(), subject, body);
    }

    @Async
    public void emailValidate(AppUserEntity userEntity) {

        String subject = "Validação de email - APP Pet System";
        String body = "Seu token de validação é: " + userEntity.getEmailToken();

        mailNotificationService.send(userEntity.getEmail(), subject, body);
    }


}
