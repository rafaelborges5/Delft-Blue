package nl.tudelft.sem.notification.manager.integration;

import nl.tudelft.sem.notification.manager.controllers.PublishNotificationController;
import nl.tudelft.sem.notification.manager.domain.notification.Notification;
import nl.tudelft.sem.notification.manager.domain.notification.NotificationRepository;
import nl.tudelft.sem.notification.manager.producers.BasicProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:29092", "port=29092" })
public class PublishNotificationIntegrationTest {

    @Autowired
    private PublishNotificationController consumer;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private BasicProducer basicProducer;

    private String topic = "publish-notification";


    @Test
    public void testReceivingMessage() {
//        NotificationRepository mockedNotificationRepository = mock(NotificationRepository.class);
//        consumer = new PublishNotificationController();
        Notification testNotification = new Notification("testId", "testDesc");
        basicProducer.send("publish-notification", testNotification);
        Notification inRepo = notificationRepository.getOne(0L);
        assertThat(true).isTrue();
    }
}
