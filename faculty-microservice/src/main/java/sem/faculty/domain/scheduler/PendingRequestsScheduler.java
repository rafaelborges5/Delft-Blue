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
 * Strategy to mark requests as pending and save them in the pending queue in the faculty.
 */
@Service
public class PendingRequestsScheduler extends SchedulableRequestsScheduler {

    @Autowired
    public RequestRepository requestRepository;

    public PendingRequestsScheduler(ScheduleRequestController controller) {
        super(controller);
    }

    @Override
    void saveRequestInFaculty(Request request, Faculty faculty, LocalDate date) {
        request.setStatus(RequestStatus.PENDING);

        // update request repository
        long requestID = request.getRequestId();
        if (requestRepository.findByRequestId(requestID) == request) {
            requestRepository.updateRequestStatusPending(requestID);
        } else {
            requestRepository.saveAndFlush(request);
        }

        faculty.addPendingRequest(request);
    }
}
