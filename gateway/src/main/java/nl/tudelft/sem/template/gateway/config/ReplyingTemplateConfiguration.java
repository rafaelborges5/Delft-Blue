package nl.tudelft.sem.template.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import sem.commons.AcceptRequestsDTO;
import sem.commons.FacultyNameDTO;
import sem.commons.PendingRequestsDTO;
import sem.commons.StatusDTO;

@Configuration
public class ReplyingTemplateConfiguration {

    @Bean
    public ReplyingKafkaTemplate<String, FacultyNameDTO, PendingRequestsDTO> replyKafkaTemplatePendingRequests(
            ProducerFactory<String, FacultyNameDTO> pf,
            KafkaMessageListenerContainer<String, PendingRequestsDTO> container
    ) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Bean
    public KafkaMessageListenerContainer<String, PendingRequestsDTO> replyContainerPendingRequests(
            ConsumerFactory<String, PendingRequestsDTO> cf) {
        ContainerProperties containerProperties = new ContainerProperties("pendingRequestsTopicReply");
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
