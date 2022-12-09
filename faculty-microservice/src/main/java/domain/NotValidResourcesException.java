package domain;

/**
 * Exception to indicate the that a resource is invalid.
 * */
public class NotValidResourcesException extends Exception {

    public NotValidResourcesException(String message) {
        super(message);
    }
}
