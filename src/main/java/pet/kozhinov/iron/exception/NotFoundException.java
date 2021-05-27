package pet.kozhinov.iron.exception;

public class NotFoundException extends IronException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
