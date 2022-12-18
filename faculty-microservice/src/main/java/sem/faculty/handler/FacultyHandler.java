package sem.faculty.handler;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.FacultyName;
import sem.faculty.domain.Request;
import sem.faculty.domain.scheduler.DenyRequestsScheduler;
import sem.faculty.domain.scheduler.PendingRequestsScheduler;
import sem.faculty.domain.scheduler.Scheduler;
import sem.faculty.provider.CurrentTimeProvider;
import sem.faculty.provider.TimeProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Component
public class FacultyHandler {
    Map<FacultyName, Faculty> faculties;
    Scheduler scheduler;
    @Autowired
    TimeProvider timeProvider;

    /**
     * Constructor method.
     *
     * @param timeProvider - TimeProvider that provides the current time.
     */
    public FacultyHandler(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        faculties = new HashMap<>();
        populateFaculties();
    }

    /**
     * Create a new Faculty Handler.
     *
     * @return a new FacultyHandler.
     */
    @Bean
    public FacultyHandler newFacultyHandler() {
        return new FacultyHandler(new CurrentTimeProvider());
    }

    /**
     * Create Faculty instances for each FacultyName.
     */
    final void populateFaculties() {
        faculties.clear();
        for (FacultyName fn : FacultyName.values()) {
            faculties.put(fn, new Faculty(fn, timeProvider));
        }
    }

    /**
     * Listen for incoming Requests.
     */
    @KafkaListener(
            topics = "incoming-request",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactory2"
    )
    void listener(Request request) {
        handleIncomingRequests(request);
    }

    /**
     * Choose how to handle an incoming Request and schedule it accordingly.
     *
     * @param request - Request to be scheduled.
     */
    void handleIncomingRequests(Request request) {
        LocalDate currentDate = timeProvider.getCurrentTime();
        LocalDate preferredDate = request.getPreferredDate();

        // if the date of the request is invalid, deny the request
        if (preferredDate.isBefore(currentDate)) {
            scheduler = new DenyRequestsScheduler();
        } else {
            scheduler = new PendingRequestsScheduler();
        }

        scheduler.scheduleRequest(request, faculties.get(request.getFacultyName()));
    }

    /**
     * Gets pending requests.
     *
     * @param facultyName the faculty name
     * @return the pending requests
     */
    public List<Request> getPendingRequests(FacultyName facultyName) {
        Faculty faculty = faculties.get(facultyName);
        //TODO: get requests from given faculty
        return new ArrayList<>();
    }
}
