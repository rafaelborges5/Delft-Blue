package sem.faculty.domain.scheduler;

import org.springframework.stereotype.Service;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.FacultyName;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;

@Service
public class PendingScheduler implements Scheduler {

    @Override
    public void scheduleRequest(Request request, Faculty faculty) {
        LocalDate date = getAvailableDate(request, faculty.getFacultyName());
        if (date == null) {
            request.setStatus(RequestStatus.DENIED);
            //TODO Could add some notifications here.
            return;
        }
        request.setStatus(RequestStatus.PENDING);
        faculty.addPendingRequest(request);
    }

    @Override
    public LocalDate getAvailableDate(Request request, FacultyName facultyName) {
        //TODO make connection to Resource Manager here and change line below.
        return request.getPreferredDate();
    }
}
