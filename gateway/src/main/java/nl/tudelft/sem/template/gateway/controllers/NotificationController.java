package nl.tudelft.sem.template.gateway.controllers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sem.commons.FacultyNameDTO;
import sem.commons.PendingRequestsDTO;

@RestController
@RequestMapping("notification/")
@Getter
public class NotificationController {

    private ReplyingKafkaTemplate<String, FacultyNameDTO, PendingRequestsDTO>
            replyingKafkaTemplatePendingNotifications;



    @Autowired
    public NotificationController(ReplyingKafkaTemplate<String, FacultyNameDTO, PendingRequestsDTO>
                                              replyingKafkaTemplatePendingNotifications) {
        this.replyingKafkaTemplatePendingNotifications = replyingKafkaTemplatePendingNotifications;
    }

    @GetMapping("{netId}")
    public void getNewNotifications(@PathVariable("netId") String netId) {
        System.out.println(netId);
    }
}
