package nl.tudelft.sem.template.authentication.tobeimportedfromothermodules;

/**
 * Exception to indicate the that a resource is invalid.
 * */
public class NotValidResourcesException extends Exception {
    static final long serialVersionUID = -3387516993124229948L + 1L;

    public NotValidResourcesException(String message) {
        super(message);
    }
}
