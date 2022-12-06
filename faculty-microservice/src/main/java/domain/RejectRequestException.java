package domain;

/**
 * Exception to indicate the that a request can not be scheduled.
 */
public class RejectRequestException extends Throwable {
    public RejectRequestException(long requestId) {
        super(String.valueOf(requestId));
    }
}
