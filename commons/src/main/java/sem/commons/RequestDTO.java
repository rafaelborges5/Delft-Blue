package sem.commons;

import java.time.LocalDate;

import lombok.*;

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
    private Resource resource;

    /**
     * secundairy constructor without requestID.
     * @param name - the request name
     * @param netId - the user NetId
     * @param faculty - the faculty to send the request to
     * @param description - the description
     * @param preferredDate - the preferred date to schedule the request
     * @param resource - the resource required for the request.
     */
    public RequestDTO(String name, String netId, FacultyName faculty, String description,
                      LocalDate preferredDate, Resource resource) {
        this.name = name;
        this.netId = netId;
        this.faculty = faculty;
        this.description = description;
        this.preferredDate = preferredDate;
        this.resource = resource;
    }
}




