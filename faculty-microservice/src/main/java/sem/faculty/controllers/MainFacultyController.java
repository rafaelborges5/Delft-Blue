package sem.faculty.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import sem.commons.FacultyNameDTO;
import sem.commons.PendingRequestsDTO;
import sem.faculty.handler.FacultyHandlerService;

@RestController
public class MainFacultyController {

    private transient FacultyHandlerService facultyHandlerService;

    @Autowired
    public MainFacultyController(FacultyHandlerService facultyHandlerService) {
        this.facultyHandlerService = facultyHandlerService;
    }

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
}
