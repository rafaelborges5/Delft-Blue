package nl.tudelft.sem.template.example;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;


/**
 * The type Consumer configuration.
 */
@Configuration
public class ConsumerConfiguration {

    private final transient KafkaProperties kafkaProperties;

    private final transient String topic;

    public ConsumerConfiguration(KafkaProperties kafkaProperties, @Value(value = "{$kafka.topic}") String topic) {
        this.kafkaProperties = kafkaProperties;
        this.topic = topic;
    }


    /**
     * Advice topic new topic.
     *
     * @return the new topic
     */
    @Bean
    public NewTopic adviceTopic() {
        return new NewTopic(topic, 3, (short) 1);
    }

    /**
     * Consumer factory.
     *
     * @return the consumer factory
     */
    @Bean
    public ConsumerFactory<String, ExampleUser> consumerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        final JsonDeserializer<ExampleUser> jsonDeserializer = new JsonDeserializer<>(ExampleUser.class, false);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
    }

    /**
     * Kafka listener container factory concurrent kafka listener container factory.
     *
     * @return the concurrent kafka listener container factory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ExampleUser> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ExampleUser> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
