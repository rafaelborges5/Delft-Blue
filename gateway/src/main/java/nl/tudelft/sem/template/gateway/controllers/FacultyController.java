package nl.tudelft.sem.template.gateway.controllers;

import nl.tudelft.sem.template.gateway.dto.FacultyNameDTO;
import nl.tudelft.sem.template.gateway.dto.PendingRequestsDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class FacultyController {

    private ReplyingKafkaTemplate<String, FacultyNameDTO, PendingRequestsDTO> replyingKafkaTemplatePendingRequests;

    @Autowired
    public FacultyController(ReplyingKafkaTemplate<String, FacultyNameDTO, PendingRequestsDTO> replyingKafkaTemplatePendingRequests) {
        this.replyingKafkaTemplatePendingRequests = replyingKafkaTemplatePendingRequests;
    }


    @PostMapping("/faculty/pending")
    public ResponseEntity<String> getPendingRequests(@RequestBody FacultyNameDTO facultyNameDTO) throws ExecutionException, InterruptedException {
        // create producer record
        ProducerRecord<String, FacultyNameDTO> record = new ProducerRecord<String, FacultyNameDTO>("pendingRequestsTopic", facultyNameDTO);
        // set reply topic in header
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "pendingRequestsTopic".getBytes()));
        // post in kafka topic
        RequestReplyFuture<String, FacultyNameDTO, PendingRequestsDTO> sendAndReceive = replyingKafkaTemplatePendingRequests.sendAndReceive(record);

        // confirm if producer produced successfully
        //SendResult<String, Model> sendResult = sendAndReceive.getSendFuture().get();
        //
        ////print all headers
        //sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

        // get consumer record
        ConsumerRecord<String, PendingRequestsDTO> consumerRecord = sendAndReceive.get();
        // return consumer value

        System.out.println("got a list from a faculty" + facultyNameDTO.getFacultyName() +
                " with status" + consumerRecord.value().getStatus());
        return ResponseEntity.ok("finished");
    }
}
