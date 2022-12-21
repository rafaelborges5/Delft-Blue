package sem.faculty.controllers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Controller;
import sem.commons.ScheduleDateDTO;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
public class ScheduleRequestController {
    private final transient ReplyingKafkaTemplate<String, ScheduleDateDTO, LocalDate>
            replyingKafkaTemplate;

    @Autowired
    public ScheduleRequestController(ReplyingKafkaTemplate<String, ScheduleDateDTO, LocalDate> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    /**
     * Send scheduleDateDTO to topic schedule-resource.
     *
     * @param scheduleDateDTO - scheduleDateDTO sent to the topic `schedule-date`
     * @return ResponseEntity with a LocalDate of the date found to be available or ResponseEntity
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public ResponseEntity<LocalDate> sendScheduleRequest(ScheduleDateDTO scheduleDateDTO)
            throws ExecutionException, InterruptedException {
        ProducerRecord<String, ScheduleDateDTO> record =
                new ProducerRecord<>("schedule-date", scheduleDateDTO);

        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "schedule-date-reply".getBytes()));

        RequestReplyFuture<String, ScheduleDateDTO, LocalDate> sendAndReceive =
                replyingKafkaTemplate.sendAndReceive(record);

        ConsumerRecord<String, LocalDate> consumerRecord;
        try {
            consumerRecord = sendAndReceive.get(20, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(consumerRecord.value());
    }
}
