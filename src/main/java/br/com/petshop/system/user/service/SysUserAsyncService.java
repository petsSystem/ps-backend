package br.com.petshop.system.user.service;

import br.com.petshop.notification.MailNotificationService;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SysUserAsyncService {
    @Autowired
    private MailNotificationService mailNotificationService;

    @Async
    public void sendNewPassword(SysUserEntity userEntity, String newPassword) {

        String subject = "Nova senha - Pet System";
        String body = "Sua nova senha é: " + newPassword;

        mailNotificationService.send(userEntity.getUsername(), subject, body);
    }

    @Async
    public void emailValidate(SysUserEntity userEntity) {

        String subject = "Validação de email - Pet System";
        String body = "Seu token de validação é: " + userEntity.getEmailToken();

        mailNotificationService.send(userEntity.getUsername(), subject, body);
    }
}

