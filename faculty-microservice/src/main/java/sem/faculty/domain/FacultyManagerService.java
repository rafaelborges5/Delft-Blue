package sem.faculty.domain;

import org.springframework.stereotype.Service;
import sem.commons.PendingRequestsDTO;
import sem.commons.RequestDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Faculty manager service.
 */
@Service
public class FacultyManagerService {


    private transient FacultyManager facultyManager;

    private transient List<String> facultyNames;

    /**
     * Instantiates a new Faculty manager service.
     *
     * @param facultyManager the faculty manager
     */
    public FacultyManagerService(FacultyManager facultyManager) {
        this.facultyManager = facultyManager;
        this.facultyNames = new ArrayList<>(List.of(new String[]{"ARCH", "MMME", "EEMCS", "IDE", "CEG", "TPM", "AE", "AS"}));
    }

    /**
     * Gets pending requests.
     *
     * @param facultyName the faculty name
     * @return the pending requests
     */
    public PendingRequestsDTO getPendingRequests(String facultyName) {
        if (!facultyNames.contains(facultyName)) {
            return new PendingRequestsDTO("Wrong faculty name", new ArrayList<>());
        } else {
            return new PendingRequestsDTO("OK",
                    facultyManager.getPendingRequests(FacultyName.valueOf(facultyName)).stream()
                            .map(x -> new RequestDTO(x.getNetId(), x.getName(), x.getDescription(),
                                    x.getPreferredDate(), x.getResource().getCpu(), x.getResource().getGpu(),
                                    x.getResource().getMemory())).collect(Collectors.toList()));
        }
    }
}
