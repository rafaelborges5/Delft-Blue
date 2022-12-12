package sem.faculty.domain;

import nl.tudelft.sem.template.gateway.dto.PendingRequestsDTO;
import nl.tudelft.sem.template.gateway.dto.RequestDTO;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyManagerService {


    private FacultyManager facultyManager;

    private List<String> facultyNames;

    public FacultyManagerService(FacultyManager facultyManager) {
        this.facultyManager = facultyManager;
        this.facultyNames = new ArrayList<>(List.of(new String[]{
                "ARCH", "MMME", "EEMCS", "IDE", "CEG", "TPM", "AE", "AS"}));
    }

    public PendingRequestsDTO getPendingRequests(String facultyName) {
        if(!facultyNames.contains(facultyName)) {
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
