package br.com.petshop.notification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailNotificationService {

    @Autowired private JavaMailSender emailSender;
    @Autowired private TemplateEngine templateEngine;

    @Async
    public void send(String mail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@petsystem.com");
        message.setTo(mail);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }

    @Async
    public void sendTemplate(String to, String subject, String templateName, Context context) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setTo(to);
            helper.setFrom("noreply@petsystem.com");
            helper.setSubject(subject);
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Handle exception
            System.out.println("Erro ao enviar email");
            e.printStackTrace();
        } finally {
            System.out.println("Enviado email");
        }
    }
}
