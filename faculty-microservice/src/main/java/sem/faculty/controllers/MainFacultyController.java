package sem.faculty.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import sem.commons.AcceptRequestsDTO;
import sem.commons.FacultyNameDTO;
import sem.commons.PendingRequestsDTO;
import sem.commons.StatusDTO;
import sem.faculty.handler.FacultyHandlerService;

/**
 * The type Main faculty controller.
 */
@RestController
public class MainFacultyController {

    private transient FacultyHandlerService facultyHandlerService;

    /**
     * Instantiates a new Main faculty controller.
     *
     * @param facultyHandlerService the faculty handler service
     */
    @Autowired
    public MainFacultyController(FacultyHandlerService facultyHandlerService) {
        this.facultyHandlerService = facultyHandlerService;
    }

    /**
     * Gets pending requests.
     *
     * @param facultyNameDTO the faculty name dto
     * @return the pending requests
     */
    @KafkaListener(
            topics = "pendingRequestsTopic",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactoryFacultyName"
    )
    @SendTo
    public PendingRequestsDTO getPendingRequests(FacultyNameDTO facultyNameDTO) {
        System.out.println("got a faculty name " + facultyNameDTO.getFacultyName());
        return facultyHandlerService.getPendingRequests(facultyNameDTO.getFacultyName());
    }

    /**
     * Accept requests status dto.
     *
     * @param acceptRequestsDTO the accept requests dto
     * @return the status dto
     */
    @KafkaListener(
            topics = "acceptRequestsTopic",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactoryAcceptRequests"
    )
    @SendTo
    public StatusDTO acceptRequests(AcceptRequestsDTO acceptRequestsDTO) {
        System.out.println("got a list of requests to accept " + acceptRequestsDTO.getAcceptedRequests().toString() +
                " from faculty " + acceptRequestsDTO.getFacultyName());
        return facultyHandlerService.acceptRequests(
                acceptRequestsDTO.getFacultyName(), acceptRequestsDTO.getAcceptedRequests()
        );
    }
}
