package pet.kozhinov.iron.entity.notification.email;

public class CaseConfirmedNotification extends Template {
    private static final String subject = "Iron Bank: Your case was confirmed";
    private static final String text = "Some of your pending cases was confirmed!" +
            "\nTo get more information visit our website.\nHave a nice day";

    public CaseConfirmedNotification(String to) {
        super(subject, to, text);
    }
}
