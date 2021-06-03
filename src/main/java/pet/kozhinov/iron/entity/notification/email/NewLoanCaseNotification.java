package pet.kozhinov.iron.entity.notification.email;

public class NewLoanCaseNotification extends Template {
    private static final String subject = "Iron Bank: a new loan offer for you";
    private static final String text = "You have a new loan offer." +
            "\nTo get more information visit our website.\nHave a nice day";

    public NewLoanCaseNotification(String to) {
        super(subject, to, text);
    }
}
