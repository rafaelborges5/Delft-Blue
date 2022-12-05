package nl.tudelft.sem.template.example;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ExampleController {

    @KafkaListener(
            topics = "example-topic",
            clientIdPrefix = "json",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, ExampleUser> record,
                        @Payload ExampleUser payload) {
        System.out.println("Registered a user with netId: " + payload.getNetId());
    }
}