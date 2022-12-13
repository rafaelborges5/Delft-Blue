package sem.faculty.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import sem.commons.FacultyNameDTO;
import sem.commons.PendingRequestsDTO;
import sem.faculty.domain.FacultyManagerService;

@RestController
public class MainFacultyController {

    private transient FacultyManagerService facultyManagerService;

    @Autowired
    public MainFacultyController(FacultyManagerService facultyManagerService) {
        this.facultyManagerService = facultyManagerService;
    }

    @KafkaListener(
            topics = "pendingRequestsTopic",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactoryFacultyName"
    )
    @SendTo
    public PendingRequestsDTO getPendingRequests(FacultyNameDTO facultyNameDTO) {
        return facultyManagerService.getPendingRequests(facultyNameDTO.getFacultyName());
    }
}
