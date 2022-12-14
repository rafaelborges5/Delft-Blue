package nl.tudelft.sem.template.gateway.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import sem.commons.ExampleUser;
import sem.commons.FacultyNameDTO;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfiguration {

    private final transient KafkaProperties kafkaProperties;

    public ProducerConfiguration(KafkaProperties kafkaProperties) {
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
     * Producer factory.
     *
     * @return the producer factory
     */
    @Bean
    public ProducerFactory<String, ExampleUser> producerFactoryExampleUser() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ProducerFactory<String, FacultyNameDTO> producerFactoryFacultyNameDTO() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Kafka template.
     *
     * @return the kafka template
     */
    @Bean
    public KafkaTemplate<String, ExampleUser> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactoryExampleUser());
    }
}
