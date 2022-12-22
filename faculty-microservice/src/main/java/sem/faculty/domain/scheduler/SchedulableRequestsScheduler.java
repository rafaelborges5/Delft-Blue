package sem.faculty.domain.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sem.commons.FacultyName;
import sem.commons.ScheduleDateDTO;
import sem.faculty.controllers.ScheduleRequestController;
import sem.faculty.domain.Faculty;
import sem.faculty.domain.NotEnoughResourcesLeftException;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestRepository;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Abstract class that has the implementation to communicate and verify the date given by the Resource Manager.
 * It only forces the child classes to implement how to store the request in each faculty.
 */
@Service
public abstract class SchedulableRequestsScheduler implements Scheduler {
    private final transient ScheduleRequestController controller;
    final transient RequestRepository requestRepository;

    @Autowired
    SchedulableRequestsScheduler(ScheduleRequestController controller, RequestRepository requestRepository) {
        this.controller = controller;
        this.requestRepository = requestRepository;
    }



    @Override
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void scheduleRequest(Request request, Faculty faculty) {

        LocalDate date = null;
        try {
            date = getAvailableDate(request, faculty.getFacultyName());
        } catch (NotEnoughResourcesLeftException e) {
            request.setStatus(RequestStatus.DENIED);
            //TODO Could add some notifications here.
            long requestID = request.getRequestId();
            if (Objects.equals(requestRepository.findByRequestId(requestID), request)) {
                requestRepository.delete(requestRepository.findByRequestId(requestID));
            }
            return;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        saveRequestInFaculty(request, faculty, date);
    }

    /**
     * Sets the status of the request and then saves it in the faculty in the appropriate place.
     *
     * @param request - Request to be saved
     * @param faculty - Faculty in which the request is saved
     * @param date - Date on which the request can be scheduled
     */
    abstract void saveRequestInFaculty(Request request, Faculty faculty, LocalDate date);


    /**
     * Ask Resource Manager for an available date if response is null than there is no available date.
     * @param request - Request to be scheduled
     * @param facultyName - FacultyName of the faculty
     */
    LocalDate getAvailableDate(Request request, FacultyName facultyName)
            throws NotEnoughResourcesLeftException, ExecutionException, InterruptedException {
        //TODO make connection to Resource Manager here and change line below.
        ScheduleDateDTO scheduleDateDTO = new ScheduleDateDTO(request.getResource(),
                request.getPreferredDate(),
                facultyName);
        ResponseEntity<LocalDate> availableDate = controller.sendScheduleRequest(scheduleDateDTO);
        if (availableDate.getBody() == null) {
            throw new NotEnoughResourcesLeftException(request.getRequestId());
        }
        return availableDate.getBody();
    }
}
