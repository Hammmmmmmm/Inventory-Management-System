public class ScreenRegistrationException extends RuntimeException {
    public ScreenRegistrationException(String message) {
        super(message);
    }

    public ScreenRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
    public ScreenRegistrationException(Throwable cause) {
        super(cause);
    }
}