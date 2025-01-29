package io.github.evaggelos99.ems.event.service.transport.kafka;

import io.github.evaggelos99.ems.kafka.lib.object.deserializer.ObjectDeserializer;
import io.github.evaggelos99.ems.kafka.lib.serializer.ByteArraySerializer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaSetupConfig {

    @Bean
    KafkaAdmin kafkaAdmin(@Value("${spring.kafka.producer.bootstrap-servers}") String producerBootstrapServers) {

        final Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    ProducerFactory<String, Serializable> producerFactory(@Value("${spring.kafka.producer.bootstrap-servers}") String producerBootstrapServers) {

        final Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "1");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaManualAckListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {

        final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    @Bean
    ConsumerFactory<String, String> consumerFactory(@Value("${spring.kafka.consumer.bootstrap-servers}") String consumerBootstrapServers) {

        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerBootstrapServers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    KafkaTemplate<String, Serializable> kafkaTemplate(final ProducerFactory<String, Serializable> producerConfigs) {

        return new KafkaTemplate<>(producerConfigs);
    }

    @Bean
    NewTopic createTopic(@Value("${io.github.evaggelos99.ems.event.topic.add-attendee}") String topicToBeCreated) {

        return new NewTopic(topicToBeCreated, 1, (short) 0);
    }

    @Bean
    ObjectDeserializer objectDeserializer() {

        return new ObjectDeserializer();
    }
}
