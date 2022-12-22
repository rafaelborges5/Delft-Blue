package sem.commons;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO representing the request made by a faculty to the resource manager
 * to fit a request on or before a given date.
 */
@Data
@AllArgsConstructor
public class ScheduleDateDTO {
    Resource resources;
    LocalDate endDate;
    FacultyName facultyName;
}
