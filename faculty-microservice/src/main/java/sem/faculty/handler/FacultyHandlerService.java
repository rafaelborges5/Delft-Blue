package sem.faculty.handler;

import org.springframework.stereotype.Service;
import sem.commons.*;
import sem.faculty.domain.Request;
import sem.faculty.domain.scheduler.AcceptRequestsScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Faculty manager service.
 */
@Service
public class FacultyHandlerService {


    private transient FacultyHandler facultyHandler;


    /**
     * Instantiates a new Faculty manager service.
     *
     * @param facultyHandler the faculty manager
     */
    public FacultyHandlerService(FacultyHandler facultyHandler) {
        this.facultyHandler = facultyHandler;
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
        FacultyName facName;
        try {
            facName = FacultyName.valueOf(facultyName);
        } catch (IllegalArgumentException e) {
            return new StatusDTO("Wrong faculty name");
        }

        //TODO: accept the requests in given faculty

        List<Long> badRequests = new ArrayList<>();
        for(Long id: acceptedRequests) {
            var request = requestRepository.findById(id);
            if (request.isEmpty()) {
                badRequests.add(id);
                continue;
            }
            Request acceptedRequest = request.get();
            facultyHandler.handleAcceptedRequests(facName, acceptedRequest);
        }
        if(badRequests.isEmpty()) {
            return new StatusDTO("OK");
        }
        return new StatusDTO("Could not find the following requests: " + badRequests.toString());
    }
}
