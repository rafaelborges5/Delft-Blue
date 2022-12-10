package nl.tudelft.sem.resource.manager.domain.node;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * DDD Value Object representing a Node's resources.
 */

@Embeddable
public class Resource {
    @Column(name = "cpu", nullable = false)
    private transient int cpuResources;

    @Column(name = "gpu", nullable = false)
    private transient int gpuResources;

    @Column(name = "memory", nullable = false)
    private transient int memResources;

    @SuppressWarnings("unused")
    public Resource() {
    }

    /**
     * Basic constructor.
     * @param cpuResources CPU resources provided by Node
     * @param gpuResources GPU resources provided by Node
     * @param memResources Memory resources provided by Node
     */
    public Resource(int cpuResources, int gpuResources, int memResources) {
        this.cpuResources = cpuResources;
        this.gpuResources = gpuResources;
        this.memResources = memResources;
    }
}
