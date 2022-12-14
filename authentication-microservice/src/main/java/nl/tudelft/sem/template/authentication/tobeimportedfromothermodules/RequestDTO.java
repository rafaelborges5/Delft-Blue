package nl.tudelft.sem.template.authentication.tobeimportedfromothermodules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import java.time.LocalDate;

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
    private FacultyName faculty;
    private String description;
    private LocalDate preferredDate;
    @Embedded
    private Resource resource;
}



