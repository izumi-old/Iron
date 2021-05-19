package pet.kozhinov.iron.exception;

public class BadRequestException extends IronException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
