package sem.faculty.domain.scheduler;

import org.springframework.stereotype.Service;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestRepository;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;

/**
 * Strategy to mark requests as accepted and save them in the schedule in the faculty.
 */
@Service
public class AcceptRequestsScheduler extends SchedulableRequestsScheduler {
    @Override
    void saveRequestInFaculty(Request request, Faculty faculty, LocalDate date, RequestRepository requestRepository) {
        request.setStatus(RequestStatus.ACCEPTED);

        // update request repository
        if (requestRepository.findByRequestId(request.getRequestId()).contains(request)) {
            requestRepository.updateRequestStatusAccepted(request.getRequestId());
        } else {
            requestRepository.saveAndFlush(request);
        }
        //TODO: reserveResources(request, scheduledDate); //Reserve the resources for request on the scheduledDate.
        faculty.scheduleForDate(request, date);
    }
}
