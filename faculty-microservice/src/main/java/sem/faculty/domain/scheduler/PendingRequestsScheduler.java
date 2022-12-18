package sem.faculty.domain.scheduler;

import org.springframework.stereotype.Service;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;

/**
 * Strategy to mark requests as pending and save them in the pending queue in the faculty.
 */
@Service
public class PendingRequestsScheduler extends SchedulableRequestsScheduler {
    @Override
    void saveRequestInFaculty(Request request, Faculty faculty, LocalDate date) {
        request.setStatus(RequestStatus.PENDING);
        faculty.addPendingRequest(request);
    }
}
