package sem.commons;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO for the request class, containing only the attributes and no logic.
 * The status was also omitted, because the status is only stored
 * in the database and not needed in between microservices.
 */
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    private long requestId;
    private String name;
    private String netId;
    private FacultyName faculty;
    private String description;
    private LocalDate preferredDate;
    private Resource resource;
}




