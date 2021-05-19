package pet.kozhinov.iron.entity.email;

public class NewLoanCaseNotification extends Template {
    public NewLoanCaseNotification(String to) {
        super("Iron Bank: a new loan offer for you", to,
                "You have a new loan offer.\nTo get more information visit our website.\nHave a nice day");
    }
}
