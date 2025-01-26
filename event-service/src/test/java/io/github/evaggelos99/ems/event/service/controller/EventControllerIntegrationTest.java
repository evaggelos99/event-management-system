package io.github.evaggelos99.ems.event.service.controller;

import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.api.util.EventObjectGenerator;
import io.github.evaggelos99.ems.event.service.EventServiceApplication;
import io.github.evaggelos99.ems.event.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.event.service.util.TestConfiguration;
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

@SpringBootTest(classes = {EventServiceApplication.class,
        TestConfiguration.class, EventControllerIntegrationTest.KafkaConfiguration.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@Testcontainers
public class EventControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost:";
    private static final String RELATIVE_ENDPOINT = "/event";
    @Container
    private final static KafkaContainer KAFKA = new KafkaContainer();
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;
    @LocalServerPort
    private int port;

    EventControllerIntegrationTest() {
        KAFKA.start();
    }

    @BeforeAll
    public void beforeAll() {

        sqlScriptExecutor.setup();
    }


    @Test
    void postEvent_getEvent_deleteEvent_getEvent_whenInvokedWithValidEventDto_thenExpectForEventToBeAddedFetchedAndDeleted() {

        final Instant currentTime = Instant.now();

        final UUID attendeeId = UUID.randomUUID();
        final UUID organizerId = UUID.randomUUID();
        final UUID sponsorId = UUID.randomUUID();

        final EventDto dto = EventObjectGenerator.generateEventDtoWithoutTimestamps(attendeeId, organizerId, sponsorId);
        // postEvent
        final ResponseEntity<EventDto> actualEntity = restTemplate.postForEntity(createUrl(), dto, EventDto.class);

        System.out.println(actualEntity);
        assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
        final EventDto actualDto = actualEntity.getBody();

        // assert
        assertNotNull(actualDto);
        assertEquals(dto.uuid(), actualDto.uuid());
        assertTrue(actualDto.createdAt().isAfter(currentTime));
        assertTrue(actualDto.lastUpdated().isAfter(currentTime));
        assertNotNull(actualDto.createdAt());
        assertNotNull(actualDto.lastUpdated());
        assertEquals(dto.name(), actualDto.name());
        assertEquals(dto.place(), actualDto.place());
        assertEquals(dto.eventType(), actualDto.eventType());
        assertTrue(actualDto.attendeesIds().contains(attendeeId));
        assertEquals(organizerId, actualDto.organizerId());
        assertEquals(dto.limitOfPeople(), actualDto.limitOfPeople());
        assertTrue(actualDto.sponsorsIds().contains(sponsorId));
        assertEquals(dto.startTimeOfEvent(), actualDto.startTimeOfEvent());
        assertEquals(dto.duration(), actualDto.duration());
        // getEvent
        final ResponseEntity<EventDto> actualGetEntity = restTemplate.getForEntity(createUrl() + "/" + actualDto.uuid(),
                EventDto.class);
        // assert
        assertTrue(actualGetEntity.getStatusCode().is2xxSuccessful());

        final EventDto getDto = actualGetEntity.getBody();
        assertNotNull(getDto);
        assertEquals(actualDto.uuid(), getDto.uuid());
        assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
                getDto.createdAt().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
        assertEquals(actualDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS),
                getDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
        assertEquals(actualDto.name(), getDto.name());
        assertEquals(actualDto.place(), getDto.place());
        assertEquals(actualDto.eventType(), getDto.eventType());
        assertEquals(actualDto.attendeesIds(), getDto.attendeesIds());
        assertEquals(actualDto.organizerId(), getDto.organizerId());
        assertEquals(actualDto.limitOfPeople(), getDto.limitOfPeople());
        assertEquals(actualDto.sponsorsIds(), getDto.sponsorsIds());
        assertEquals(actualDto.startTimeOfEvent(), getDto.startTimeOfEvent());
        assertEquals(actualDto.duration(), getDto.duration());

        // deleteEvent
        final ResponseEntity<Void> deletedEntity = restTemplate.exchange(createUrl() + "/" + actualDto.uuid(),
                HttpMethod.DELETE, null, Void.class);
        assertTrue(deletedEntity.getStatusCode().is2xxSuccessful());
        // assertThat it cannot be found
        final ResponseEntity<EventDto> deletedDto = restTemplate.getForEntity(createUrl() + "/" + actualDto.uuid(),
                EventDto.class);
        assertTrue(deletedDto.getStatusCode().is2xxSuccessful());
        assertNull(deletedDto.getBody());
    }

    private String createUrl() {

        return HOSTNAME + port + RELATIVE_ENDPOINT;
    }

    @Test
    void postEvent_putEvent_getEvent_deleteEvent_getAll_whenInvokedWithValidEventDto_thenExpectForEventToBeAddedThenEditedThenDeleted() {

        final Instant currentTime = Instant.now();

        final UUID attendeeId = UUID.randomUUID();
        final UUID organizerId = UUID.randomUUID();
        final UUID sponsorId = UUID.randomUUID();

        final EventDto dto = EventObjectGenerator.generateEventDtoWithoutTimestamps(attendeeId, organizerId, sponsorId);
        // postEvent
        final ResponseEntity<EventDto> actualEntity = restTemplate.postForEntity(createUrl(), dto, EventDto.class);

        assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
        final EventDto actualDto = actualEntity.getBody();

        // assert
        assertNotNull(actualDto);
        assertEquals(dto.uuid(), actualDto.uuid());
        assertTrue(actualDto.createdAt().isAfter(currentTime));
        assertTrue(actualDto.lastUpdated().isAfter(currentTime));
        assertNotNull(actualDto.createdAt());
        assertNotNull(actualDto.lastUpdated());
        assertEquals(dto.name(), actualDto.name());
        assertEquals(dto.place(), actualDto.place());
        assertEquals(dto.eventType(), actualDto.eventType());
        assertTrue(actualDto.attendeesIds().contains(attendeeId));
        assertEquals(organizerId, actualDto.organizerId());
        assertEquals(dto.limitOfPeople(), actualDto.limitOfPeople());
        assertTrue(actualDto.sponsorsIds().contains(sponsorId));
        assertEquals(dto.startTimeOfEvent(), actualDto.startTimeOfEvent());
        assertEquals(dto.duration(), actualDto.duration());

        final UUID newOrganizerId = UUID.randomUUID();
        final EventDto updatedDto = EventObjectGenerator.generateEventDtoWithExistingIdWithoutTimestamps(
                actualDto.uuid(), attendeeId, newOrganizerId, sponsorId);

        final ResponseEntity<EventDto> actualPutEntity = restTemplate.exchange(createUrl() + "/" + actualDto.uuid(),
                HttpMethod.PUT, createHttpEntity(updatedDto), EventDto.class);
        final EventDto actualPutDto = actualPutEntity.getBody();
        // assert
        assertTrue(actualPutEntity.getStatusCode().is2xxSuccessful());

        assertNotNull(actualPutDto);
        assertEquals(actualDto.uuid(), actualPutDto.uuid());
        assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
                actualPutDto.createdAt().truncatedTo(ChronoUnit.MILLIS));
        assertTrue(actualPutDto.lastUpdated().isAfter(actualDto.lastUpdated()));
        assertEquals(updatedDto.organizerId(), actualPutDto.organizerId());
        // deleteEvent
        restTemplate.delete(createUrl() + "/" + actualDto.uuid());
        // assertThat the list returned is empty
        @SuppressWarnings("rawtypes") final ResponseEntity<List> listOfEventDtos = restTemplate.getForEntity(createUrl(), List.class);
        assertTrue(listOfEventDtos.getStatusCode().is2xxSuccessful());
        final List<?> body = listOfEventDtos.getBody();
        assertNotNull(body);
        assertTrue(body.isEmpty());
    }

    @SuppressWarnings({"all"})
    private HttpEntity createHttpEntity(final EventDto updatedDto) {

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
