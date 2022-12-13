package nl.tudelft.sem.template.gateway.controllers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sem.commons.FacultyNameDTO;
import sem.commons.PendingRequestsDTO;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The type Faculty controller.
 */
@RestController
public class FacultyController {

    private transient ReplyingKafkaTemplate<String, FacultyNameDTO, PendingRequestsDTO> replyingKafkaTemplatePendingRequests;

    /**
     * Instantiates a new Faculty controller.
     *
     * @param replyingKafkaTemplatePendingRequests the replying kafka template pending requests
     */
    @Autowired
    public FacultyController(
            ReplyingKafkaTemplate<String, FacultyNameDTO, PendingRequestsDTO> replyingKafkaTemplatePendingRequests) {
        this.replyingKafkaTemplatePendingRequests = replyingKafkaTemplatePendingRequests;
    }


    /**
     * Gets pending requests.
     *
     * @param facultyNameDTO the faculty name dto
     * @return the pending requests
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     */
    @PostMapping("/faculty/pending")
    public ResponseEntity<String> getPendingRequests(
            @RequestBody FacultyNameDTO facultyNameDTO) throws ExecutionException, InterruptedException, TimeoutException {
        // create producer record
        ProducerRecord<String, FacultyNameDTO> record =
                new ProducerRecord<String, FacultyNameDTO>("pendingRequestsTopic", facultyNameDTO);
        // set reply topic in header
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "pendingRequestsTopicReply".getBytes()));
        // post in kafka topic
        RequestReplyFuture<String, FacultyNameDTO, PendingRequestsDTO> sendAndReceive =
                replyingKafkaTemplatePendingRequests.sendAndReceive(record);

        //confirm if producer produced successfully
        SendResult<String, FacultyNameDTO> sendResult = sendAndReceive.getSendFuture().get(10, TimeUnit.SECONDS);

        //print all headers
        //sendResult.getProducerRecord().headers()
        // .forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

        // get consumer record
        ConsumerRecord<String, PendingRequestsDTO> consumerRecord = sendAndReceive.get(10, TimeUnit.SECONDS);
        // return consumer value

        System.out.println("got a list from a faculty " + facultyNameDTO.getFacultyName() +
                " with status " + consumerRecord.value().getStatus() + ". The list is " +
                consumerRecord.value().getRequests().toString());
        return ResponseEntity.ok("finished and printed to console");
    }
}
