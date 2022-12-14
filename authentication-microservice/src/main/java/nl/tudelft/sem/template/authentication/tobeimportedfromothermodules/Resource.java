package nl.tudelft.sem.template.authentication.tobeimportedfromothermodules;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@ToString
@EqualsAndHashCode
public class Resource {

    @Column(name = "resource_cpu", nullable = false)
    private int cpu;
    @Column(name = "resource_gpu", nullable = false)
    private int gpu;
    @Column(name = "resource_memory", nullable = false)
    private int memory;

    @SuppressWarnings("unused")
    public Resource() {
    }

    /**
     * Constructor for Resource class.
     *
     * @param cpu - integer of cpu resources needed
     * @param gpu - integer of gpu resources needed
     * @param memory - integer of memory resources needed
     * @throws NotValidResourcesException - when the resource restrictions are not met.
     */
    public Resource(int cpu, int gpu, int memory) throws NotValidResourcesException {
        checkResourceValidity(cpu, gpu, memory);

        this.cpu = cpu;
        this.gpu = gpu;
        this.memory = memory;
    }

    /**
     * Checks the constraint for the resource class.
     * Called in the constructor before making the object.
     * @param cpu - cpu of resource
     * @param gpu - gpu of resource
     * @param memory - memory of resource
     * @throws NotValidResourcesException - when either negative values are found or other constraints are broken
     */
    public void checkResourceValidity(int cpu, int gpu, int memory) throws NotValidResourcesException {
        if (cpu < 0 || gpu < 0 || memory < 0) {
            throw new NotValidResourcesException("Resource cannot have negative values");
        }

        //The business logic for issue #23
        if (cpu < memory || cpu < gpu) {
            throw new NotValidResourcesException("The cpu resources should be equal to at least max(memory, gpu)");
        }
    }
}
