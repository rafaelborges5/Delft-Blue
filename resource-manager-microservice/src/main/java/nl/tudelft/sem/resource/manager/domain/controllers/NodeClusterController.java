package nl.tudelft.sem.resource.manager.domain.controllers;

import nl.tudelft.sem.resource.manager.domain.Resource;
import nl.tudelft.sem.resource.manager.domain.services.ResourceAvailabilityService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
public class NodeClusterController {

    private final transient ResourceAvailabilityService resourceAvailabilityService;

    @Autowired
    public NodeClusterController(ResourceAvailabilityService resourceAvailabilityService) {
        this.resourceAvailabilityService = resourceAvailabilityService;
    }


    @KafkaListener(
            topics = "user-view",
            groupId = "default",
            containerFactory = "kafkaListenerContainerFactoryLocalDate"
    )
    @SendTo
    public Resource getUserViewResourcesForDate(ConsumerRecord<String, LocalDate> record, @Payload LocalDate localDate)
    {
        return resourceAvailabilityService.seeFreeResourcesOnDate(localDate);
    }



}
