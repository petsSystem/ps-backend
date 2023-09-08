package br.com.petshop.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailNotificationService {

    @Autowired
    private JavaMailSender emailSender;
    public void send(String mail, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@petsystem.com");
        message.setTo(mail);
        message.setSubject("Nova senha - APP Pet System");
        message.setText("Sua nova senha é: " + newPassword);
        emailSender.send(message);
    }

}
