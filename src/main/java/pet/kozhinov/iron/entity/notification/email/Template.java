package pet.kozhinov.iron.entity.notification.email;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Data
public abstract class Template implements pet.kozhinov.iron.entity.notification.email.Email {
    private String from;

    @NotNull
    private final String subject;

    @Email
    private final String to;

    @NotNull
    private final String text;
}
