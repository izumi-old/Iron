package pet.kozhinov.iron.exception;

public class InternalServerErrorException extends IronException {
    public InternalServerErrorException() {
        super();
    }

    public InternalServerErrorException(String message) {
        super(message);
    }
}
