package sem.faculty.handler;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Component;
import sem.commons.FacultyName;
import sem.commons.RequestDTO;
import sem.commons.ScheduleDateDTO;
import sem.faculty.controllers.ScheduleRequestController;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.Request;
import sem.faculty.domain.scheduler.AcceptRequestsScheduler;
import sem.faculty.domain.scheduler.DenyRequestsScheduler;
import sem.faculty.domain.scheduler.PendingRequestsScheduler;
import sem.faculty.domain.scheduler.Scheduler;
import sem.faculty.provider.CurrentTimeProvider;
import sem.faculty.provider.TimeProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Component
public class FacultyHandler {
    Map<FacultyName, Faculty> faculties;
    Scheduler scheduler;
    @Autowired
    TimeProvider timeProvider;
    @Autowired
    ScheduleRequestController scheduleRequestController = null;

    /**
     * Constructor method.
     */
    public FacultyHandler() {
        this.timeProvider = new CurrentTimeProvider();
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
        return new FacultyHandler();
    }

    /**
     * Create Faculty instances for each FacultyName.
     */
    private void populateFaculties() {
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
        LocalDate currentDate = timeProvider.getCurrentDate();
        LocalDate preferredDate = request.getPreferredDate();

        // if the date of the request is invalid, deny the request
        if (preferredDate.isBefore(currentDate) || isInfiveMinutesBeforePreferredDay(preferredDate)) {
            scheduler = new DenyRequestsScheduler();
        } else if (isInSixHoursBeforePreferredDay(preferredDate)) {
            scheduler = new AcceptRequestsScheduler(scheduleRequestController);
        } else {
            scheduler = new PendingRequestsScheduler(scheduleRequestController);
        }

        scheduler.scheduleRequest(request, faculties.get(request.getFacultyName()));
    }

    /**
     * Returns true if time is within 5 minutes of the preferred day, false otherwise.
     * @param preferredDate - preferred Date to schedule request
     * @return boolean
     */
    boolean isInfiveMinutesBeforePreferredDay(LocalDate preferredDate) {
        LocalDateTime preferredDateStart = preferredDate.atStartOfDay();
        LocalDateTime currentTime = timeProvider.getCurrentDateTime();
        return currentTime.plusMinutes(5).isAfter(preferredDateStart) ||
                currentTime.plusMinutes(5).isEqual(preferredDateStart);
    }

    /**
     * Returns true if time is within 6 hours of the preferred day, false otherwise.
     * @param preferredDate - preferred Date to schedule request
     * @return boolean
     */
    boolean isInSixHoursBeforePreferredDay(LocalDate preferredDate) {
        LocalDateTime preferredDateStart = preferredDate.atStartOfDay();
        LocalDateTime currentTime = timeProvider.getCurrentDateTime();
        return currentTime.plusHours(6).isAfter(preferredDateStart) ||
                currentTime.plusHours(6).isEqual(preferredDateStart);
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

    /**
     * Gets request for date.
     *
     * @param date the date
     * @return the request for date
     */
    public Map<FacultyName, List<RequestDTO>> getRequestForDate(LocalDate date) {
        Map<FacultyName, List<RequestDTO>> map = new HashMap<>();

        for (FacultyName facultyName : FacultyName.values()) {
            map.put(facultyName, faculties.get(facultyName).getRequestsForDate(date));
        }

        return map;
    }

    /**
     * Schedule all pending requests for next day in all faculties.
     */
    public void acceptPendingRequestsForTomorrow() {
        scheduler = new AcceptRequestsScheduler(scheduleRequestController);

        for (Faculty faculty : faculties.values()) {
            List<Request> requests = getPendingRequestsForTomorrow(faculty);

            for (Request request : requests) {
                scheduler.scheduleRequest(request, faculty);
            }
        }
    }

    /**
     * Get all pendingRequests for tomorrow from faculty.
     * @param faculty - Faculty from which to get the requests
     * @return - List of Requests
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public List<Request> getPendingRequestsForTomorrow(Faculty faculty) {
        LocalDate tomorrow = timeProvider.getCurrentDate();
        tomorrow = tomorrow.plusDays(1);

        List<Request> tomorrowList = new ArrayList<>();
        List<Request> pendingRequests = faculty.getPendingRequests();

        for (Request request : pendingRequests) {
            LocalDate date = request.getPreferredDate();
            if (tomorrow.equals(date)) {
                tomorrowList.add(request);
            } else {
                faculty.addPendingRequest(request);
            }
        }

        return tomorrowList;
    }
}
