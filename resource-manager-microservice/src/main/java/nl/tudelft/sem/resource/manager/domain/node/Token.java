package nl.tudelft.sem.resource.manager.domain.node;

import lombok.EqualsAndHashCode;

/**
 * A DDD Value Object representing the Token required to access the Node.
 */

@EqualsAndHashCode
public class Token {
    private final transient String tokenValue;

    public Token(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @Override
    public String toString() {
        return tokenValue;
    }
}
