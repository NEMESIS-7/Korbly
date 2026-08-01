package com.arete.korbly.modules.shared.service;

import com.arete.korbly.modules.shared.dto.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;


    private static final String appEmail = "elikemfenuksu@gmail.com";

    public EmailService(JavaMailSender javaMailSender,
                        TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }


    @Async
    public void sendEmail(EmailRequest request, String template, Context context) {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String htmlContent = templateEngine.process(template, context);

            helper.setFrom(appEmail);
            helper.setTo(request.recipient());
            helper.setSubject(request.subject());
            helper.setText(htmlContent, true);

            System.out.println("email: " + htmlContent);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
