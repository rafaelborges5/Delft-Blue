package nl.tudelft.sem.notification.manager.controllers;

import nl.tudelft.sem.notification.manager.domain.notification.Notification;
import nl.tudelft.sem.notification.manager.domain.notification.NotificationRepository;
import nl.tudelft.sem.notification.manager.producers.BasicProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=29092"})
public class PublishNotificationsTest {

    @Autowired
    private PublishNotificationController consumer;
    private NotificationRepository notificationRepository;
    private String topic = "publish-notification";


    @Test
    public void testReceivingMessage() {
        Notification testNotification = new Notification("testId", "testDesc");
        notificationRepository = Mockito.mock(NotificationRepository.class);
        consumer = new PublishNotificationController(notificationRepository);
        consumer.saveNotification(new ConsumerRecord<>(topic, 1, 0, "sd", testNotification), testNotification);
        verify(notificationRepository).saveAndFlush(testNotification);
    }
}