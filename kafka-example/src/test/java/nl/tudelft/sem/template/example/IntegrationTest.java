package nl.tudelft.sem.template.example;

import nl.tudelft.sem.template.gateway.*;
import nl.tudelft.sem.template.kafka.KafkaConsumer;
import nl.tudelft.sem.template.kafka.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = Application.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties =
        {"listeners=PLAINTEXT://localhost:8083", "port=8083"})
public class IntegrationTest {

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    @Value("${test.topic}")
    private String topic;

    @Test
    public void givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived()
            throws Exception {
        String data = "Sending with our own simple KafkaProducer";

        producer.send(topic, data);

        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assert(messageConsumed);
        assert(consumer.getPayload().contains(data));
    }
}
