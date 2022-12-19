package sem.faculty.domain.scheduler;

import org.springframework.stereotype.Service;
import sem.commons.FacultyName;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;

/**
 * Abstract class that has the implementation to communicate and verify the date given by the Resource Manager.
 * It only forces the child classes to implement how to store the request in each faculty.
 */
@Service
public abstract class SchedulableRequestsScheduler implements Scheduler {
    @Override
    public void scheduleRequest(Request request, Faculty faculty) {
        LocalDate date = getAvailableDate(request, faculty.getFacultyName());
        if (date == null) {
            request.setStatus(RequestStatus.DENIED);
            //TODO Could add some notifications here.
            return;
        }
        saveRequestInFaculty(request, faculty, date);
    }

    /**
     * Sets the status of the request and then saves it in the faculty in the appropriate place.
     *
     * @param request - Request to be saved
     * @param faculty - Faculty in which the request is saved
     * @param date - Date on which the request can be scheduled
     */
    abstract void saveRequestInFaculty(Request request, Faculty faculty, LocalDate date);


    /**
     * Ask Resource Manager for an available date if response is null than there is no available date.
     * @param request - Request to be scheduled
     * @param facultyName - FacultyName of the faculty
     */
    LocalDate getAvailableDate(Request request, FacultyName facultyName) {
        //TODO make connection to Resource Manager here and change line below.
        return request.getPreferredDate();
    }
}
