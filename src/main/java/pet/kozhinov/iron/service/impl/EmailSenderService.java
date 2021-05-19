package pet.kozhinov.iron.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pet.kozhinov.iron.entity.email.Template;

import javax.validation.Valid;

@Slf4j
@Service
public class EmailSenderService {
    private final JavaMailSender mailSender;
    private final String from;

    public EmailSenderService(@Value("${email.username}") String from, JavaMailSender mailSender) {
        this.from = from;
        this.mailSender = mailSender;
    }

    @Validated
    @Async
    public void send(@Valid Template template) {
        try {
            mailSender.send(generateSimpleEmailMessage(template));
        } catch (MailException ex) {
            log.debug(ex.getMessage());
        }
    }

    private SimpleMailMessage generateSimpleEmailMessage(Template template) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject(template.getSubject());
        simpleMailMessage.setText(template.getText());
        simpleMailMessage.setTo(template.getTo());
        return simpleMailMessage;
    }
}
