package io.github.evaggelos99.ems.attendee.service.controller;

import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import io.github.evaggelos99.ems.attendee.api.util.AttendeeObjectGenerator;
import io.github.evaggelos99.ems.attendee.service.AttendeeServiceApplication;
import io.github.evaggelos99.ems.attendee.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.attendee.service.util.TestConfiguration;
import io.github.evaggelos99.ems.kafka.lib.object.deserializer.ObjectDeserializer;
import io.github.evaggelos99.ems.kafka.lib.serializer.ByteArraySerializer;
import io.github.evaggelos99.ems.testcontainerkafka.lib.KafkaContainer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {AttendeeServiceApplication.class,
        TestConfiguration.class, AttendeeControllerIntegrationTest.KafkaConfiguration.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@Testcontainers
class AttendeeControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost:";
    private static final String RELATIVE_ENDPOINT = "/attendee";
    @Container
    private final static KafkaContainer KAFKA = new KafkaContainer();
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;
    @LocalServerPort
    private int port;

    AttendeeControllerIntegrationTest() {
        KAFKA.start();

        System.setProperty("spring.kafka.producer.bootstrap-servers", KAFKA.getBootstrapServers());
    }

    @BeforeAll
    void beforeAll() {

        this.sqlScriptExecutor.setup();
    }

    @Test
    void postAttendee_getAttendee_deleteAttendee_getAttendee_whenInvokedWithValidAttendeeDto_thenExpectForAttendeeToBeAddedFetchedAndDeleted() {

        final Instant currentTime = Instant.now();
        final AttendeeDto dto = AttendeeObjectGenerator.generateAttendeeDtoWithoutTimestamps();
        // postAttendee
        final ResponseEntity<AttendeeDto> actualEntity = this.restTemplate.postForEntity(this.createUrl(), dto,
                AttendeeDto.class);
        assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
        final AttendeeDto actualDto = actualEntity.getBody();
        // assert
        assertNotNull(actualDto);
        assertEquals(dto.uuid(), actualDto.uuid());
        assertTrue(actualDto.createdAt().isAfter(currentTime));
        assertTrue(actualDto.lastUpdated().isAfter(currentTime));
        assertEquals(dto.firstName(), actualDto.firstName());
        assertEquals(dto.lastName(), actualDto.lastName());
        assertTrue(actualDto.ticketIDs().isEmpty());
        // getAttendee
        final ResponseEntity<AttendeeDto> actualGetEntity = this.restTemplate
                .getForEntity(this.createUrl() + "/" + actualDto.uuid(), AttendeeDto.class);
        // assert
        assertTrue(actualGetEntity.getStatusCode().is2xxSuccessful());
        final AttendeeDto getDto = actualGetEntity.getBody();
        assertNotNull(getDto);
        assertEquals(actualDto.uuid(), getDto.uuid());
        assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
                getDto.createdAt().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
        assertEquals(actualDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS),
                getDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
        assertEquals(actualDto.firstName(), getDto.firstName());
        assertEquals(actualDto.lastName(), getDto.lastName());
        assertEquals(actualDto.ticketIDs(), getDto.ticketIDs());

        // deleteAttendee
        final ResponseEntity<Void> deletedEntity = this.restTemplate.exchange(this.createUrl() + "/" + actualDto.uuid(),
                HttpMethod.DELETE, null, Void.class);
        assertTrue(deletedEntity.getStatusCode().is2xxSuccessful());
        // assertThat it cannot be found
        final ResponseEntity<AttendeeDto> deletedDto = this.restTemplate
                .getForEntity(this.createUrl() + "/" + actualDto.uuid(), AttendeeDto.class);
        assertTrue(deletedDto.getStatusCode().is2xxSuccessful());
        assertNull(deletedDto.getBody());
    }

    private String createUrl() {

        return HOSTNAME + this.port + RELATIVE_ENDPOINT;
    }

    @Test
    void postAttendee_putAttendee_getAttendee_deleteAttendee_getAll_whenInvokedWithValidAttendeeDto_thenExpectForAttendeeToBeAddedThenEditedThenDeleted() {

        final Instant currentTime = Instant.now();
        final UUID ticketId = UUID.randomUUID();
        final AttendeeDto dto = AttendeeObjectGenerator.generateAttendeeDtoWithoutTimestamps(ticketId);
        // postAttendee
        final ResponseEntity<AttendeeDto> actualEntity = this.restTemplate.postForEntity(this.createUrl(), dto,
                AttendeeDto.class);
        assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
        final AttendeeDto actualDto = actualEntity.getBody();
        // assert
        assertNotNull(actualDto);
        assertEquals(dto.uuid(), actualDto.uuid());
        assertTrue(actualDto.createdAt().isAfter(currentTime));
        assertTrue(actualDto.lastUpdated().isAfter(currentTime));
        assertEquals(dto.firstName(), actualDto.firstName());
        assertEquals(dto.lastName(), actualDto.lastName());
        assertTrue(actualDto.ticketIDs().size() == 1);
        assertTrue(actualDto.ticketIDs().contains(ticketId));
        // putAttendee
        final UUID differentTicketId = UUID.randomUUID();
        final AttendeeDto updatedDto = new AttendeeDto(actualDto.uuid(), null, null, UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(differentTicketId));
        final ResponseEntity<AttendeeDto> actualPutEntity = this.restTemplate.exchange(
                this.createUrl() + "/" + actualDto.uuid(), HttpMethod.PUT, this.createHttpEntity(updatedDto),
                AttendeeDto.class);
        final AttendeeDto actualPutDto = actualPutEntity.getBody();
        // assert
        assertTrue(actualPutEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(actualPutDto);
        assertEquals(actualPutDto.uuid(), actualDto.uuid());
        assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
                actualPutDto.createdAt().truncatedTo(ChronoUnit.MILLIS));
        assertTrue(actualPutDto.lastUpdated().isAfter(actualDto.lastUpdated()));
        assertEquals(updatedDto.firstName(), actualPutDto.firstName());
        assertEquals(updatedDto.lastName(), actualPutDto.lastName());
        assertTrue(updatedDto.ticketIDs().size() == 1);
        assertTrue(updatedDto.ticketIDs().contains(differentTicketId));
        // deleteAttendee
        this.restTemplate.delete(this.createUrl() + "/" + actualDto.uuid());
        // assertThat the list returned is empty
        @SuppressWarnings("rawtypes") final ResponseEntity<List> listOfAttendeeDtos = this.restTemplate.getForEntity(this.createUrl(), List.class);
        assertTrue(listOfAttendeeDtos.getStatusCode().is2xxSuccessful());
        final List<?> body = listOfAttendeeDtos.getBody();
        assertNotNull(body);
        assertTrue(body.isEmpty());
    }

    @SuppressWarnings({"all"})
    private HttpEntity createHttpEntity(final AttendeeDto updatedDto) {

        return new HttpEntity(updatedDto);
    }

    @org.springframework.boot.test.context.TestConfiguration
    static class KafkaConfiguration {

        @Value("${io.github.evaggelos99.ems.event.topic.add-attendee}")
        private String topicToBeCreated;

        @Bean("kafkaAdmin")
        KafkaAdmin kafkaAdmin() {

            final Map<String, Object> configs = new HashMap<>();
            configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());
            return new KafkaAdmin(configs);
        }

        @Bean("producerFactory")
        ProducerFactory<String, Serializable> producerFactory() {

            final Map<String, Object> configProps = new HashMap<>();
            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
            configProps.put(ProducerConfig.ACKS_CONFIG, "1");
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean("kafkaManualAckListenerContainerFactory")
        public ConcurrentKafkaListenerContainerFactory<String, String> kafkaManualAckListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {

            final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory);
            factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
            return factory;
        }

        @Bean("consumerFactory")
        ConsumerFactory<String, String> consumerFactory() {

            final Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "default-group");
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
            props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

            return new DefaultKafkaConsumerFactory<>(props);
        }

        @Bean("kafkaTemplate")
        KafkaTemplate<String, Serializable> kafkaTemplate(final ProducerFactory<String, Serializable> producerConfigs) {

            return new KafkaTemplate<>(producerConfigs);
        }

        @Bean("createTopic")
        NewTopic createTopic() {

            return new NewTopic(topicToBeCreated, 1, (short) 0);
        }

        @Bean("objectDeserializer")
        ObjectDeserializer objectDeserializer() {

            return new ObjectDeserializer();
        }
    }
}
