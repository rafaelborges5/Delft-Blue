package sem.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * The type Request dto.
 */
@Data
public class RequestDTO {
    private String netId;
    private String name;
    private String description;
    private LocalDate preferredDate;
    private int cpu;
    private int gpu;
    private int memory;


    /**
     * Instantiates a new Request dto.
     *
     * @param netId         the net id
     * @param name          the name
     * @param description   the description
     * @param preferredDate the preferred date
     * @param cpu           the cpu
     * @param gpu           the gpu
     * @param memory        the memory
     */
    public RequestDTO(
            @JsonProperty("netId") String netId,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("preferredDate") LocalDate preferredDate,
            @JsonProperty("cpu") int cpu,
            @JsonProperty("gpu") int gpu,
            @JsonProperty("memory") int memory
    ) {
        this.netId = netId;
        this.name = name;
        this.description = description;
        this.preferredDate = preferredDate;
        this.cpu = cpu;
        this.gpu = gpu;
        this.memory = memory;
    }
}
