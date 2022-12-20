package nl.tudelft.sem.resource.manager.domain.controllers;

import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.resource.Reserver;
import nl.tudelft.sem.resource.manager.domain.services.ResourceAvailabilityService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import sem.commons.FacultyNameDTO;
import sem.commons.FacultyNamePackageDTO;
import sem.commons.NotValidResourcesException;
import sem.commons.RegularUserView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class NodeClusterController {

    private final transient ResourceAvailabilityService resourceAvailabilityService;

    @Autowired
    public NodeClusterController(ResourceAvailabilityService resourceAvailabilityService) {
        this.resourceAvailabilityService = resourceAvailabilityService;
    }


    /**
     * This method will allow users to see the amount of available resources for their faculties for the following day.
     * @param record the Consumer Record
     * @param faculties the Package that contains all the faculties the user belongs
     * @return A Regular user view, which is basically a map from faculty name to resources available
     */
    @KafkaListener(
            topics = "user-view",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactoryFacultyNamePackageDTO"
    )
    @SendTo
    public RegularUserView getUserViewResourcesForDate(ConsumerRecord<String, FacultyNamePackageDTO> record,
                                                       @Payload FacultyNamePackageDTO faculties) {
        Map<FacultyNameDTO, sem.commons.Resource> map = new HashMap<>();
        faculties.getFaculties().forEach(x -> {
            Resource resourceObject = resourceAvailabilityService
                    .seeFreeResourcesTomorrow(Reserver.valueOf(x.getFacultyName().toUpperCase(Locale.UK)));

            try {
                map.put(x, new sem.commons.Resource(resourceObject.getCpuResources(), resourceObject.getGpuResources(),
                        resourceObject.getMemResources()));
            } catch (NotValidResourcesException e) {
                throw new RuntimeException(e);
            }
        });
        return new RegularUserView(map);
    }



}
