package domain;

/**
 * Exception to indicate the that a request can not be scheduled.
 * */
public class NotEnoughResourcesLeftException extends Throwable {
    public NotEnoughResourcesLeftException(long requestId) {
        super(String.valueOf(requestId));
    }
}
