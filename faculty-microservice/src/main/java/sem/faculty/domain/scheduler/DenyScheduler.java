package sem.faculty.domain.scheduler;

import org.springframework.stereotype.Service;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.FacultyName;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;

@Service
public class DenyScheduler implements Scheduler {

    @Override
    public void scheduleRequest(Request request, Faculty faculty) {
        request.setStatus(RequestStatus.DENIED);
        //TODO Could add some notifications here.
    }

    @Override
    public LocalDate getAvailableDate(Request request, FacultyName facultyName) {
        //No connection needed as requests are denied without any additional checks.
        return null;
    }
}
