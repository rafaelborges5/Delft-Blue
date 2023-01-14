package sem.commons;

//import javax.persistence.Column;
//import javax.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

//@Embeddable
@Data
public class Resource {

    private int cpu;
    private int gpu;
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
        if (checkResourcesNonNegative(cpu, gpu, memory)) {
            throw new NotValidResourcesException("Resource cannot have negative values");
        }

        //The business logic for issue #23
        if (checkResourcesRelationalConstraint(cpu, gpu, memory)) {
            throw new NotValidResourcesException("The cpu resources should be equal to at least max(memory, gpu)");
        }
    }

    /**
     * This method will check the constraint that all the values for the resources are positive.
     * @param cpu the cpu value
     * @param gpu the gpu value
     * @param memory the memory value
     * @return a boolean representing if the check was passed or no
     */
    public boolean checkResourcesNonNegative(int cpu, int gpu, int memory) {
        return (cpu < 0 || gpu < 0 || memory < 0);
    }

    /**
     * This method will check the relational constraints between the resources values. These are that cpu < memory and
     * that cpu < gpu.
     * @param cpu the cpu value
     * @param gpu the gpu value
     * @param memory the memory value
     * @return a boolean representing if the check was passed or no
     */
    public boolean checkResourcesRelationalConstraint(int cpu, int gpu, int memory) {
        return (cpu < memory || cpu < gpu);
    }

}
