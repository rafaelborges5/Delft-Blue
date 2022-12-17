package sem.commons;

import java.time.LocalDate;

/**
 * DTO representing the request made by a faculty to the resource manager
 * to fit a request on or before a given date.
 */

public class ScheduleDateDTO {
    Resources resources;
    LocalDate endDate;
    String facultyName;
}
