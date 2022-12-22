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
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sem.commons.DateDTO;
import sem.commons.FacultyNamePackageDTO;
import sem.commons.RegularUserView;
import sem.commons.SysadminUserView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("userView/")
public class UserViewController {

    private final transient ReplyingKafkaTemplate<String, FacultyNamePackageDTO, RegularUserView>
            replyingKafkaTemplateUserView;

    private final transient ReplyingKafkaTemplate<String, DateDTO, SysadminUserView
            getReplyingKafkaTemplateSysadminView;

    private final transient ReplyingKafkaTemplate<String, View>

    private final transient ReplyingKafkaTemplate<String, DateDTO, SysadminUserView>
            replyingKafkaTemplateSysadminView;


    private final transient AuthManager authManager;

    @Autowired
    public UserViewController(
            ReplyingKafkaTemplate<String, FacultyNamePackageDTO, RegularUserView> replyingKafkaTemplateUserView,
            ReplyingKafkaTemplate<String, DateDTO, SysadminUserView> replyingKafkaTemplateSysadminView,
            AuthManager authManager) {
        this.replyingKafkaTemplateUserView = replyingKafkaTemplateUserView;
        this.replyingKafkaTemplateSysadminView = replyingKafkaTemplateSysadminView;
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
                replyingKafkaTemplateUserView.sendAndReceive(record);

        ConsumerRecord<String, RegularUserView> consumerRecord;
        try {
            consumerRecord = sendAndReceive.get(10, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(consumerRecord.value());
    }

    @PostMapping("/sysadmin")
    public ResponseEntity<SysadminUserView> getSysadminView(@Payload DateDTO dateDTO) {
        ProducerRecord<String, DateDTO> record =
                new ProducerRecord<>("sysadmin-view", dateDTO);

        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "sysadmin-view-reply".getBytes()));


    }

    @PostMapping("/sysAdmin")
    public ResponseEntity<SysadminUserView> getSysAdminView(@Payload DateDTO dateDTO)
            throws ExecutionException, InterruptedException, TimeoutException {
         ProducerRecord<String, DateDTO> record =
                 new ProducerRecord<>("sysadmin-view", dateDTO);

        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "sysadmin-view-reply".getBytes()));

        RequestReplyFuture<String, DateDTO, SysadminUserView> sendAndReceive =
                replyingKafkaTemplateSysadminView.sendAndReceive(record);

        ConsumerRecord<String, SysadminUserView> consumerRecord = sendAndReceive.get(10, TimeUnit.SECONDS);

        return ResponseEntity.ok(consumerRecord.value());
    }


}
