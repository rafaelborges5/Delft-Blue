package nl.tudelft.sem.resource.manager.config;

import nl.tudelft.sem.resource.manager.domain.Resource;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * This class has all the configurations that allow for the consuming and replying of messages. There is some
 * duplication with the ProducerConfiguration and ConsumerConfiguration classes but this way it improves readability
 * and understanding of what is needed to consume and produce messages
 */

@EnableKafka
@Configuration
public class KafkaReplyingConsumerConfig {

    private transient KafkaProperties kafkaProperties;

    public KafkaReplyingConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * This method will return the producerConfiguration to be used in publishing messages.
     * @return the configs
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * This method will return the consumer configs to be later used in consuming messages from topics.
     * @return the configs
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, LocalDate> consumerFactoryLocalDate() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
                new StringDeserializer(), new JsonDeserializer<>(LocalDate.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LocalDate> kafkaListenerContainerFactoryLocalDate() {
        ConcurrentKafkaListenerContainerFactory<String, LocalDate> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryLocalDate());

        //Setup of reply template
        factory.setReplyTemplate(kafkaTemplateUserView());

        return factory;
    }

    @Bean
    public KafkaTemplate<String, Resource> kafkaTemplateUserView() {
        return new KafkaTemplate<>(producerFactoryUserView());
    }

    @Bean
    public ProducerFactory<String, Resource> producerFactoryUserView() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
}
