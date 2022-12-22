package sem.faculty.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import sem.commons.*;
import sem.faculty.domain.Request;
import sem.faculty.domain.RequestRepository;
import sem.faculty.domain.RequestStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Faculty manager service.
 */
@Service
public class FacultyHandlerService {


    private transient FacultyHandler facultyHandler;
    public final transient RequestRepository requestRepository;


    /**
     * Instantiates a new Faculty manager service.
     *
     * @param facultyHandler the faculty manager
     * @param requestRepository - Repository that stores all the requests that belong to all the faculties.
     */
    @Autowired
    public FacultyHandlerService(FacultyHandler facultyHandler,
                                  RequestRepository requestRepository) {
        this.facultyHandler = facultyHandler;
        this.requestRepository = requestRepository;
    }


    /**
     * Converts the RequestDTO to a regular pending request Object and forwards it to the specific faculty.
     *
     * @param request the RequestDTO sent from the MainFacultyController
     */
    public void requestListener(RequestDTO request) {
        String requestName = request.getName();
        String requestNetId = request.getNetId();
        String requestDescription = request.getDescription();
        LocalDate requestDate = request.getPreferredDate();
        Resource requestResources = request.getResource();
        Request newRequest = new Request(requestName, requestNetId, requestDescription, requestDate,
                RequestStatus.PENDING, request.getFaculty(), requestResources);
        facultyHandler.handleIncomingRequests(newRequest);
    }



    /**
     * Deletes the request from the repository if it was dropped or denied.
     *
     * @param request - Request that has to be deleted from the repository
     */
    public void deleteRequest(Request request) {
        long requestID = request.getRequestId();
        Request requestFound = requestRepository.findByRequestId(requestID);
        if (requestFound == request) {
            requestRepository.delete(requestFound);
        }
    }


    /**
     * Makes changes to the requestRepository if the request was accepted.
     *
     * @param request - Request that has been accepted.
     */
    public void acceptRequest(Request request) {
        long requestID = request.getRequestId();
        Request requestFound = requestRepository.findByRequestId(requestID);
        if (requestFound == request) {
            requestRepository.updateRequestStatusAccepted(request.getRequestId());
        } else {
            requestRepository.saveAndFlush(request);
        }
    }

    /**
     * Gets pending requests.
     *
     * @param facultyName the faculty name
     * @return the pending requests
     */
    public PendingRequestsDTO getPendingRequests(String facultyName) {

        try {
            FacultyName.valueOf(facultyName);
        } catch (IllegalArgumentException e) {
            return new PendingRequestsDTO("Wrong faculty name", new ArrayList<>());
        }
        return new PendingRequestsDTO("OK",
                facultyHandler.getPendingRequests(FacultyName.valueOf(facultyName)).stream()
                        .map(x -> new RequestDTO(
                                x.getRequestId(),
                                x.getName(),
                                x.getNetId(),
                                x.getFacultyName(),
                                x.getDescription(),
                                x.getPreferredDate(),
                                x.getResource()))
                        .collect(Collectors.toList()));

    }

    /**
     * Accept requests status dto.
     *
     * @param facultyName      the faculty name
     * @param acceptedRequests the accepted requests
     * @return the status dto
     */
    public StatusDTO acceptRequests(String facultyName, List<Long> acceptedRequests) {

        try {
            FacultyName.valueOf(facultyName);
        } catch (IllegalArgumentException e) {
            return new StatusDTO("Wrong faculty name");
        }

        //TODO: accept the requests in given faculty (also check if there requests even exist)

        return new StatusDTO("OK");
    }
}
