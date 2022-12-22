package sem.faculty.domain.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestRepository;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Strategy class that automatically denies requests.
 */
@Service
public class DenyRequestsScheduler implements Scheduler {

    @Autowired
    public RequestRepository requestRepository;

    @Override
    public void scheduleRequest(Request request, Faculty faculty) {
        request.setStatus(RequestStatus.DENIED);

        // update request repository
        long requestID = request.getRequestId();
        if (requestRepository.findByRequestId(requestID) == request) {
            requestRepository.delete(requestRepository.findByRequestId(requestID));
        }
        //TODO Could add some notifications here.
    }
}
