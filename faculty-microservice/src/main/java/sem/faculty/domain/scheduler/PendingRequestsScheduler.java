package sem.faculty.domain.scheduler;

import org.springframework.stereotype.Service;
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
    @Override
    void saveRequestInFaculty(Request request, Faculty faculty, LocalDate date, RequestRepository requestRepository) {
        request.setStatus(RequestStatus.PENDING);

        // update request repository
        if (requestRepository.findByRequestId(request.getRequestId()).contains(request)) {
            requestRepository.updateRequestStatusPending(request.getRequestId());
        } else {
            requestRepository.saveAndFlush(request);
        }

        faculty.addPendingRequest(request);
    }
}
