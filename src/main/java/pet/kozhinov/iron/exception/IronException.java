package pet.kozhinov.iron.exception;

public class IronException extends RuntimeException {
    public IronException() {
        super();
    }

    public IronException(String message) {
        super(message);
    }

    public IronException(String message, Throwable cause) {
        super(message, cause);
    }

    public IronException(Throwable cause) {
        super(cause);
    }
}
