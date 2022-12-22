package sem.faculty.handler;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sem.commons.FacultyName;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestRepository;
import sem.faculty.domain.scheduler.DenyRequestsScheduler;
import sem.faculty.domain.scheduler.PendingRequestsScheduler;
import sem.faculty.domain.scheduler.Scheduler;
import sem.faculty.provider.CurrentTimeProvider;
import sem.faculty.provider.TimeProvider;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Component
public class FacultyHandler {
    Map<FacultyName, Faculty> faculties;
    Scheduler scheduler;
    TimeProvider timeProvider;
    RequestRepository requestRepository;

    /**
     * Constructor method.
     */
    public FacultyHandler() {
        this.timeProvider = new CurrentTimeProvider();
        faculties = new HashMap<>();
        populateFaculties();
    }

    /**
     * Method to set the requestRepository that is being used in the following methods.
     *
     * @param requestRepository the repository storing the request
     */
    public void setRequestRepository(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    /**
     * Create a new Faculty Handler.
     *
     * @return a new FacultyHandler.
     */
    @Bean
    public FacultyHandler newFacultyHandler() {
        return new FacultyHandler();
    }

    /**
     * Create Faculty instances for each FacultyName.
     */
    private void populateFaculties() {
        faculties.clear();
        for (FacultyName fn : FacultyName.values()) {
            Faculty faculty = new Faculty(fn, timeProvider);
            faculties.put(fn, faculty);
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
    void listener(Request request, RequestRepository requestRepository) {
        handleIncomingRequests(request, requestRepository);
    }

    /**
     * Choose how to handle an incoming Request and schedule it accordingly.
     *
     * @param request - Request to be scheduled.
     * @param requestRepository - Repository that stores the requests to be scheduled.
     */
    void handleIncomingRequests(Request request, RequestRepository requestRepository) {
        LocalDate currentDate = timeProvider.getCurrentTime();
        LocalDate preferredDate = request.getPreferredDate();

        // if the date of the request is invalid, deny the request

        if (preferredDate.isBefore(currentDate)) {
            scheduler = new DenyRequestsScheduler();
        } else {
            scheduler = new PendingRequestsScheduler();
        }
        scheduler.scheduleRequest(request, faculties.get(request.getFacultyName()), requestRepository);
    }

    /**
     * Gets pending requests.
     *
     * @param facultyName the faculty name
     * @return the pending requests
     */
    public List<Request> getPendingRequests(FacultyName facultyName) {
        Faculty faculty = faculties.get(facultyName);
        return faculty.getPendingRequests();
    }
}
