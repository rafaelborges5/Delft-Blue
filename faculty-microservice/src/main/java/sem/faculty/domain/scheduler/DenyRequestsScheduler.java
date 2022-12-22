package sem.faculty.domain.scheduler;

import org.springframework.stereotype.Service;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestRepository;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;

/**
 * Strategy class that automatically denies requests.
 */
@Service
public class DenyRequestsScheduler implements Scheduler {
    @Override
    public void scheduleRequest(Request request, Faculty faculty, RequestRepository requestRepository) {
        request.setStatus(RequestStatus.DENIED);

        // update request repository
        if (requestRepository.findByRequestId(request.getRequestId()).contains(request)) {
            requestRepository.updateRequestStatusDenied(request.getRequestId());
        } else {
            requestRepository.saveAndFlush(request);
        }
        //TODO Could add some notifications here.
    }
}
