package br.com.petshop.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

@RestController
@RequestMapping("/api/v1/pet/notification")
public class NotificationController {
    @Autowired private MailNotificationService service;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public void get (@RequestParam("tipo") String tipo) {
        MailType type = MailType.NEW_PASSWORD;

        if (tipo.equals("email"))
            type = MailType.VALIDATE_EMAIL;
        else if (tipo.equals("app"))
            type = MailType.APP_INVITATION;

        service.sendEmail("Vanessa Paula Arronche",
                "varronche1@gmail.com" , generateToken(), type);
    }

    private String generateToken() {
        int number = (int) (100000 + Math.random() * (999999 - 100000 + 1));
        return String.valueOf(number);
    }
}
