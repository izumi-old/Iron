package pet.kozhinov.iron.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.notification.email.Email;
import pet.kozhinov.iron.service.NotificationService;

@Slf4j
@Service
public class EmailService implements NotificationService<Email> {
    private final JavaMailSender mailSender;
    private final String from;

    @Override
    public void sendAsynchronous(Email email) {
        try {
            mailSender.send(generateSimpleEmailMessage(email));
        } catch (MailException ex) {
            log.error(ex.getMessage());
        }
    }

    public EmailService(@Value("${email.username}") String from, JavaMailSender mailSender) {
        this.from = from;
        this.mailSender = mailSender;
    }

    private SimpleMailMessage generateSimpleEmailMessage(Email email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject(email.getSubject());
        simpleMailMessage.setText(email.getText());
        simpleMailMessage.setTo(email.getTo());
        return simpleMailMessage;
    }
}
