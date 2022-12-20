package nl.tudelft.sem.template.gateway.controllers;

import nl.tudelft.sem.template.gateway.authentication.AuthManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sem.commons.FacultyNamePackageDTO;
import sem.commons.RegularUserView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("userView/")
public class UserViewController {

    private final transient ReplyingKafkaTemplate<String, FacultyNamePackageDTO, RegularUserView>
            resourceReplyingKafkaTemplateUserView;

    private final transient AuthManager authManager;

    @Autowired
    public UserViewController(ReplyingKafkaTemplate<String, FacultyNamePackageDTO, RegularUserView>
                                          resourceReplyingKafkaTemplateUserView, AuthManager authManager) {
        this.resourceReplyingKafkaTemplateUserView = resourceReplyingKafkaTemplateUserView;
        this.authManager = authManager;
    }

    /**
     * This is the endpoint that users must target in order to see the available resources for the following
     * day for the faculties they belong to.
     * @return a class that basically maps faculty names (their faculties) to the amount of available resources
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @GetMapping("/regularUser")
    public ResponseEntity<RegularUserView> getNormalUserView() {
        ProducerRecord<String, FacultyNamePackageDTO> record =
                new ProducerRecord<>("user-view", new FacultyNamePackageDTO(authManager.getFaculties()));

        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "user-view-reply".getBytes()));

        RequestReplyFuture<String, FacultyNamePackageDTO, RegularUserView> sendAndReceive =
                resourceReplyingKafkaTemplateUserView.sendAndReceive(record);

        ConsumerRecord<String, RegularUserView> consumerRecord;
        try {
            consumerRecord = sendAndReceive.get(10, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(consumerRecord.value());
    }


}
