package pet.kozhinov.iron.entity.notification.email;

import pet.kozhinov.iron.entity.notification.Notification;

public interface Email extends Notification {
    String getFrom();
    String getSubject();
    String getTo();
    String getText();
}
