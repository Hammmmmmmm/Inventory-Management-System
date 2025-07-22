package Exceptions;
public class InsufficientPermissionsException extends Exception {
    public InsufficientPermissionsException(String message) {
        super(message);
    }
}