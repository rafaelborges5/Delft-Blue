package nl.tudelft.sem.resource.manager.domain.node;

import lombok.EqualsAndHashCode;

/**
 * A DDD Value Object representing the URL at which the node is located.
 */

@EqualsAndHashCode
public class URL {
    private final transient String urlValue;

    public URL(String urlValue) {
        this.urlValue = urlValue;
    }

    @Override
    public String toString() {
        return urlValue;
    }
}
