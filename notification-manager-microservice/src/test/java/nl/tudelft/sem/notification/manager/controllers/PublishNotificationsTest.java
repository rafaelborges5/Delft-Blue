package nl.tudelft.sem.notification.manager.controllers;

import nl.tudelft.sem.notification.manager.domain.notification.Notification;
import nl.tudelft.sem.notification.manager.producers.BasicProducer;
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

    @Autowired
    private BasicProducer basicProducer;

    private String topic = "publish-notification";


    @Test
    public void testReceivingMessage() {
        PublishNotificationController spiedConsumer = Mockito.spy(consumer);
        Notification testNotification = new Notification("testId", "testDesc");
        basicProducer.send("publish-notification", testNotification);
        verify(consumer).saveNotification(any(), Mockito.eq(testNotification));
    }
}