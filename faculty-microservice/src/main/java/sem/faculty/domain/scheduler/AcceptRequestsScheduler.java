package sem.faculty.domain.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sem.faculty.controllers.ScheduleRequestController;
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

    @Autowired
    public RequestRepository requestRepository;

    public AcceptRequestsScheduler(ScheduleRequestController controller) {
        super(controller);
    }

    @Override
    void saveRequestInFaculty(Request request, Faculty faculty, LocalDate date) {
        request.setStatus(RequestStatus.ACCEPTED);

        // update request repository
        long requestID = request.getRequestId();
        if (requestRepository.findByRequestId(requestID) == request) {
            requestRepository.updateRequestStatusAccepted(requestID);
        } else {
            requestRepository.saveAndFlush(request);
        }
        //TODO: reserveResources(request, scheduledDate); //Reserve the resources for request on the scheduledDate.
        faculty.scheduleForDate(request, date);
    }
}
