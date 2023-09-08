package br.com.petshop.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailNotificationService mailService;

//    @GetMapping("/send")
//    @ResponseStatus(HttpStatus.OK)
//    public void create() {
//        mailService.send();
//    }
}
