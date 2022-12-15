package sem.faculty.handler;

import org.springframework.stereotype.Service;
import sem.commons.PendingRequestsDTO;
import sem.commons.RequestDTO;
import sem.commons.FacultyName;

import java.util.ArrayList;
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
}
