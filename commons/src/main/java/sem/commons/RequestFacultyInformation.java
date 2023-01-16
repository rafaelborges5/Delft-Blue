package sem.commons;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RequestFacultyInformation {

    private LocalDate preferredDate;
    private FacultyName faculty;
    private String netId;

    /**
     * The constructor for the RequestFacultyInformation.
     * @param preferredDate the preferred date of the request
     * @param faculty the faculty of the request
     * @param netId the netId of the owner of the request
     */
    public RequestFacultyInformation(LocalDate preferredDate, FacultyName faculty, String netId) {
        this.preferredDate = preferredDate;
        this.faculty = faculty;
        this.netId = netId;
    }
}
