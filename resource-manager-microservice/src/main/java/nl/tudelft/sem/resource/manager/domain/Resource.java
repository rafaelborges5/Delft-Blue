package nl.tudelft.sem.resource.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * DDD Value Object representing a Node's resources.
 */

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Column(name = "cpu", nullable = false)
    private transient int cpuResources;

    @Column(name = "gpu", nullable = false)
    private transient int gpuResources;

    @Column(name = "memory", nullable = false)
    private transient int memResources;
}
