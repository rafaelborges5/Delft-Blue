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
    Resource() {
    }

    /**
     * Constructor for Resource class.
     *
     * @param cpu - integer of cpu resources needed
     * @param gpu - integer of gpu resources needed
     * @param memory - integer of memory resources needed
     */
    public Resource(int cpu, int gpu, int memory) {
        this.cpu = cpu;
        this.gpu = gpu;
        this.memory = memory;
    }
}
