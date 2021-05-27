package pet.kozhinov.iron.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import pet.kozhinov.iron.entity.notification.Notification;

import javax.validation.Valid;

@Validated
public interface NotificationService<T extends Notification> {

    @Async
    void sendAsynchronous(@Valid T t);
}
