package nl.tudelft.sem.template.gateway.dto;

import java.time.LocalDate;

import lombok.Data;

/**
 * Data storage
 */
@Data
public class RequestDTO {

    private long requestId;
    private String netId;
    private String name;
    private String description;
    private LocalDate preferredDate;
    private Resource resource;

    /**
     * Empty Constructor.
     */
    public RequestDTO() {
    }

    /**
     * Constructor method.
     */
    public RequestDTO(String name, String netId, String description,
                   LocalDate preferredDate, Resource resource) {
        this.name = name;
        this.netId = netId;
        this.description = description;
        this.preferredDate = preferredDate;
        this.resource = resource;
    }
}



