package io.github.evaggelos99.ems.attendee.service.transport.kafka;

import io.github.evaggelos99.ems.kafka.lib.serializer.ByteArraySerializer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "kafka", name = "enabled", havingValue = "true", matchIfMissing = true)
public class Config {

//    @Value("${io.github.evaggelos99.ems.event.topic.add-attendee}")
//    private String topicToBeCreated;

    @Bean
    KafkaAdmin kafkaAdmin(@Value("${spring.kafka.producer.bootstrap-servers}") final String bootstrapServers) {

        final Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    ProducerFactory<String, Serializable> producerFactory(@Value("${spring.kafka.producer.bootstrap-servers}") final String bootstrapServers) {

        final Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "1");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    KafkaTemplate<String, Serializable> kafkaTemplate(final ProducerFactory<String, Serializable> producerConfigs) {

        return new KafkaTemplate<>(producerConfigs);
    }

//    @Bean
//    NewTopic createTopic() {
//
//        return new NewTopic(topicToBeCreated, 0, (short) 0);
//    }

    @Bean
    WebClient.Builder webClientBuilder() {

        return WebClient.builder();
    }
}
