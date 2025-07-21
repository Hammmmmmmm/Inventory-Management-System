package Exceptions;

public class MultipleUserLoginException extends RuntimeException{
    public MultipleUserLoginException(String message) {
        super(message);
    }
    
    public MultipleUserLoginException(String message, Throwable cause){
        super(message, cause);
    }

    public MultipleUserLoginException(Throwable cause) {
        super(cause);
    }
}
