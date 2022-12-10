package domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
        this.cpu = cpu;
        this.gpu = gpu;
        this.memory = memory;

        if (this.cpu < 0 || this.gpu < 0 || this.memory < 0) {
            throw new NotValidResourcesException("Resource cannot have negative values");
        }

        //The business logic for issue #23
        if (this.cpu < this.memory || this.cpu < this.gpu) {
            throw new NotValidResourcesException("The cpu resources should be equal to at least max(memory, gpu)");
        }
    }
}
