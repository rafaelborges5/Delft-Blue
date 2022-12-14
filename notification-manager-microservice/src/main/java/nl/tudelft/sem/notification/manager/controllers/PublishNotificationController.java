package nl.tudelft.sem.notification.manager.controllers;

import nl.tudelft.sem.notification.manager.domain.notification.Notification;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

public class PublishNotificationController {

    @KafkaListener(
            topics = "publish-notification",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumerNotification(ConsumerRecord<String, Notification> record, @Payload Notification payload) {
        System.out.println("asdsfassd");
    }
}
