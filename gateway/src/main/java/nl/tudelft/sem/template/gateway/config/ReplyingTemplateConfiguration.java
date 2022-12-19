package nl.tudelft.sem.template.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import sem.commons.*;

/**
 * This class has all the configurations that allow for the consuming and replying of messages. There is some
 * duplication with the ProducerConfiguration and ConsumerConfiguration classes but this way it improves readability
 * and understanding of what is needed to consume and produce messages
 */
@Configuration
public class ReplyingTemplateConfiguration {

    /**
     * This method will set up the kafkaTemplate for Consuming and Replying to messages.
     * @param pf the producerFactory to reply
     * @param container the listener container
     * @return the kafkaTemplate
     */
    @Bean
    public ReplyingKafkaTemplate<String, FacultyNameDTO, PendingRequestsDTO> replyKafkaTemplatePendingRequests(
            ProducerFactory<String, FacultyNameDTO> pf,
            KafkaMessageListenerContainer<String, PendingRequestsDTO> container
    ) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    /**
     * This method will return the container used in replyKafkaTemplatePendingRequests.
     * @param cf the consumer factory
     * @return the listenerContainer
     */
    @Bean
    public KafkaMessageListenerContainer<String, PendingRequestsDTO> replyContainerPendingRequests(
            ConsumerFactory<String, PendingRequestsDTO> cf) {
        ContainerProperties containerProperties = new ContainerProperties("pendingRequestsTopicReply");
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    /**
     * This method will set up the kafkaTemplate for Consuming and Replying to messages.
     * @param pf the producerFactory to reply
     * @param container the listener container
     * @return the kafkaTemplate
     */
    @Bean
    public ReplyingKafkaTemplate<String, NetIdDTO, NotificationPackage> replyKafkaTemplatePendingNotifications(
            ProducerFactory<String, NetIdDTO> pf,
            KafkaMessageListenerContainer<String, NotificationPackage> container
    ) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    /**
     * This method will return the container used in replyKafkaTemplatePendingNotifications.
     * @param cf the consumer factory
     * @return the listenerContainer
     */
    @Bean
    public KafkaMessageListenerContainer<String, NotificationPackage> replyContainerPendingNotifications(
            ConsumerFactory<String, NotificationPackage> cf) {
        ContainerProperties containerProperties = new ContainerProperties("poll-notifications-reply");
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ReplyingKafkaTemplate<String, AcceptRequestsDTO, StatusDTO> replyKafkaTemplateAcceptRequests(
            ProducerFactory<String, AcceptRequestsDTO> pf,
            KafkaMessageListenerContainer<String, StatusDTO> container
    ) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Bean
    public KafkaMessageListenerContainer<String, StatusDTO> replyContainerAcceptRequests(
            ConsumerFactory<String, StatusDTO> cf) {
        ContainerProperties containerProperties = new ContainerProperties("acceptRequestsTopicReply");
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }


}
