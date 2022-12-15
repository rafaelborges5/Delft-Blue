package sem.faculty.handler;

import org.springframework.stereotype.Service;
import sem.commons.AcceptRequestsDTO;
import sem.commons.PendingRequestsDTO;
import sem.commons.RequestDTO;
import sem.commons.StatusDTO;
import sem.faculty.domain.FacultyName;
import sem.faculty.handler.FacultyHandler;

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
                        .map(x -> new RequestDTO(x.getNetId(), x.getName(), x.getDescription(),
                                x.getPreferredDate(), x.getResource().getCpu(), x.getResource().getGpu(),
                                x.getResource().getMemory())).collect(Collectors.toList()));

    }

    /**
     * Accept requests status dto.
     *
     * @param facultyName      the faculty name
     * @param acceptedRequests the accepted requests
     * @return the status dto
     */
    public StatusDTO acceptRequests(String facultyName, List<RequestDTO> acceptedRequests) {

        try {
            FacultyName.valueOf(facultyName);
        } catch (IllegalArgumentException e) {
            return new StatusDTO("Wrong faculty name");
        }

        //TODO: accept the requests in given faculty (also check if there requests even exist)

        return new StatusDTO("OK");
    }
}
