package nl.tudelft.sem.template.gateway.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import sem.commons.PendingRequestsDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Consumer configuration.
 */
@Configuration
public class ConsumerConfiguration {

    private final transient KafkaProperties kafkaProperties;

    /**
     * Instantiates a new Consumer configuration.
     *
     * @param kafkaProperties the kafka properties
     */
    public ConsumerConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Consumer configs map.
     *
     * @return the map
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }

    /**
     * Consumer factory faculty name consumer factory.
     *
     * @return the consumer factory
     */
    @Bean
    public ConsumerFactory<String, PendingRequestsDTO> consumerFactoryFacultyName() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
                new StringDeserializer(), new JsonDeserializer<>(PendingRequestsDTO.class));
    }
}