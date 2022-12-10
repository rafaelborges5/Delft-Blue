package nl.tudelft.sem.resource.manager.domain.node;

import lombok.EqualsAndHashCode;

/**
 * A DDD value object representing the name of the Node.
 */

@EqualsAndHashCode
public class OwnerName {
    private final transient String name;

    public OwnerName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
