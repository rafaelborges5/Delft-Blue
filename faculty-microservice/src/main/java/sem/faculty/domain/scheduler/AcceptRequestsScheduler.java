package sem.faculty.domain.scheduler;

import org.springframework.stereotype.Service;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;

/**
 * Strategy to mark requests as accepted and save them in the schedule in the faculty.
 */
@Service
public class AcceptRequestsScheduler extends SchedulableRequestsScheduler {
    @Override
    void saveRequestInFaculty(Request request, Faculty faculty, LocalDate date) {
        request.setStatus(RequestStatus.ACCEPTED);
        //TODO: reserveResources(request, scheduledDate); //Reserve the resources for request on the scheduledDate.
        faculty.scheduleForDate(request, date);
    }
}
