package br.com.petshop.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

@RestController
@RequestMapping("/api/v1/pet/notification")
public class NotificationController {
    @Autowired private MailNotificationService service;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public void get () {

        Context context = new Context();
        context.setVariable("title", "Vanessa");

        context.setVariable("message", "Sua nova senha de acesso é:");

        context.setVariable("token", "27a1be77");


        service.sendTemplate("varronche1@gmail.com", "PetHound - System", "email-template", context);
    }
}
