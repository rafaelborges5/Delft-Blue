package nl.tudelft.sem.template.gateway.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.gateway.commons.Resource;

import javax.persistence.Embedded;

/**
 * DTO for the request class, containing only the attributes and no logic.
 * The status was also omitted, because the status is only stored
 * in the database and not needed in between microservices.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    private long requestId;
    private String name;
    private String netId;
    private String description;
    private LocalDate preferredDate;
    @Embedded
    private Resource resource;
}



