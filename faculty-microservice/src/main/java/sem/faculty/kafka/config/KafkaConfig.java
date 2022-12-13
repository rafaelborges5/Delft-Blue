package sem.faculty.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import sem.commons.FacultyNameDTO;
import sem.commons.PendingRequestsDTO;


/**
 * The type Kafka config.
 */
@EnableKafka
@Configuration
public class KafkaConfig {

    private transient KafkaProperties kafkaProperties;

    /**
     * Instantiates a new Kafka config.
     *
     * @param kafkaProperties the kafka properties
     */
    public KafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Producer configs map.
     *
     * @return the map
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * Consumer configs map.
     *
     * @return the map
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * Consumer factory faculty name consumer factory.
     *
     * @return the consumer factory
     */
    @Bean
    public ConsumerFactory<String, FacultyNameDTO> consumerFactoryFacultyName() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
                new StringDeserializer(), new JsonDeserializer<>(FacultyNameDTO.class));
    }

    /**
     * Kafka listener container factory faculty name concurrent kafka listener container factory.
     *
     * @return the concurrent kafka listener container factory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FacultyNameDTO> kafkaListenerContainerFactoryFacultyName() {
        ConcurrentKafkaListenerContainerFactory<String, FacultyNameDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryFacultyName());

        //Setup of reply template
        factory.setReplyTemplate(kafkaTemplatePendingRequests());

        return factory;
    }

    /**
     * Kafka template pending requests kafka template.
     *
     * @return the kafka template
     */
    @Bean
    public KafkaTemplate<String, PendingRequestsDTO> kafkaTemplatePendingRequests() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Producer factory producer factory.
     *
     * @return the producer factory
     */
    @Bean
    public ProducerFactory<String, PendingRequestsDTO> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
}