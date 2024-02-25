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

/**
 * Classe responsável pelos serviços de notificação do sistema.
 */
@Service
public class MailNotificationService {

    @Autowired private JavaMailSender emailSender;
    @Autowired private TemplateEngine templateEngine;

    private final String TEMPLATE_NAME = "email-template";

    @Async
    public void sendEmail(String name, String mail, String token, MailType type) {

        String firstName = name.split(" ")[0];
        String message = type.get();
        String subject = "PetHound - "+ message + token;

        Context context = setContext(firstName, message, token);

        sendTemplate(mail, subject, TEMPLATE_NAME, context);
    }

    private Context setContext(String name, String message, String token) {
        Context context = new Context();

        context.setVariable("name", name);
        context.setVariable("message", message);
        context.setVariable("token", token);

        return context;
    }

    private void sendTemplate(String to, String subject, String templateName, Context context) {
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
            throw new RuntimeException();
        }
    }

    private void send(String mail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@petsystem.com");
        message.setTo(mail);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }
}
