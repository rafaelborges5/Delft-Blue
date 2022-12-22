package sem.faculty.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sem.faculty.handler.FacultyHandlerService;

@Service
public class TimerService {

    private transient FacultyHandlerService facultyHandlerService;

    @Autowired
    public TimerService(FacultyHandlerService facultyHandlerService) {
        this.facultyHandlerService = facultyHandlerService;
    }

    /**
     * Schedule all pending requests for next day in the facultyHandlerService each day at 18:00PM.
     */
    @Scheduled(cron = "0 0 18 * * ?")
    public void acceptPendingRequestsForTomorrow() {
        facultyHandlerService.acceptPendingRequestsForTomorrow();
    }
}
