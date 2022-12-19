package nl.tudelft.sem.notification.manager.producers;

import lombok.Getter;
import nl.tudelft.sem.notification.manager.domain.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BasicProducer {

    @Autowired
    private KafkaTemplate<String, Notification> kafkaTemplate;

    public void send(String topic, Notification payload) {
        kafkaTemplate.send(topic, payload);
    }

}
