package br.com.petshop.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailNotificationService {

    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void send(String mail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@petsystem.com");
        message.setTo(mail);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }
}
