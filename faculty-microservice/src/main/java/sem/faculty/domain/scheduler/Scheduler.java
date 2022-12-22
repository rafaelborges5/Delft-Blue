package sem.faculty.domain.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.Request;

import java.time.LocalDate;

/**
 * An interface used to implement the Strategy pattern.
 */
@Service
public interface Scheduler {
    /**
     * Schedule the request in faculty.
     * @param request - Request that will be scheduled
     * @param faculty - Faculty in which the request will be scheduled.
     */
    void scheduleRequest(Request request, Faculty faculty);
}
