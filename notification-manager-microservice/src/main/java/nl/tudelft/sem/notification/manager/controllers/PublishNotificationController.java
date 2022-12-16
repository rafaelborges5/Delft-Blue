package nl.tudelft.sem.notification.manager.controllers;

import nl.tudelft.sem.notification.manager.domain.notification.Notification;
import nl.tudelft.sem.notification.manager.domain.notification.NotificationRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class PublishNotificationController {

    private final NotificationRepository notificationRepository;

    @Autowired
    public PublishNotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = "publish-notification",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void saveNotification(ConsumerRecord<String, Notification> record, @Payload Notification payload) {
        System.out.println(payload.toString());
        notificationRepository.saveAndFlush(payload);
    }
}
