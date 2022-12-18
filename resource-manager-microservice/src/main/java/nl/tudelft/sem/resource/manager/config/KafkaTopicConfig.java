package nl.tudelft.sem.resource.manager.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.Topic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration for the topics used by the microservice.
 * These are primarily used to communicate with faculty-microservice or user-microservice
 */

@Configuration
public class KafkaTopicConfig {

    /**
     * Instantiates a new Topic.
     * This topic should be used for communication with the faculty-microservice to
     * establish on what date the Request should be serviced.
     *
     * @return a new Topic
     */
    @Bean
    public NewTopic fitRequestTopic() {
        return TopicBuilder.name("fit-request")
                .build();
    }
}
