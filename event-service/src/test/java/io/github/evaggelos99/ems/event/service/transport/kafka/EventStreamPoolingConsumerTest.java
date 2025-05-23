package io.github.evaggelos99.ems.event.service.transport.kafka;

import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.service.EventServiceApplication;
import io.github.evaggelos99.ems.event.service.repository.EventRepository;
import io.github.evaggelos99.ems.event.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.testcontainerkafka.lib.ExtendedKafkaContainer;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes = {EventServiceApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
class EventStreamPoolingConsumerTest {

    @Container
    private static final ExtendedKafkaContainer KAFKA = new ExtendedKafkaContainer();
    @Container
    static PostgreSQLContainer PG = new PostgreSQLContainer<>("postgres:16-alpine")
            .withUsername("event-management-system-user")
            .withPassword("event-management-system-user")
            .withDatabaseName("event-management-system")
            .withExposedPorts(5432);
    static {

        KAFKA.start();
        PG.setWaitStrategy(new LogMessageWaitStrategy().withRegEx(".*database system is ready to accept connections.*")
                .withStartupTimeout(Duration.of(15, ChronoUnit.SECONDS)));
        PG.start();
    }

    @AfterAll
    static void tearDown() {
        PG.stop();
        KAFKA.stop();
    }

    @Value("${io.github.evaggelos99.ems.event.topic.event-streaming-prefix}")
    private String topicPrefix;
    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;
    @Autowired
    private KafkaTemplate<String, Serializable> kafkaTemplate;
    @Autowired
    private KafkaAdmin kafkaAdmin;
    @Autowired
    private EventRepository eventRepository;
    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void configureKafkaProperties(DynamicPropertyRegistry registry) {

        final String kafkaUrl = KAFKA.getHost() + ":" + KAFKA.getFirstMappedPort();
        registry.add("kafka.enabled", () -> true);
        registry.add("spring.kafka.consumer.bootstrap-servers", () -> kafkaUrl);
        registry.add("spring.kafka.producer.bootstrap-servers", () -> kafkaUrl);
        registry.add("io.github.evaggelos99.ems.event-service.db.username", PG::getUsername);
        registry.add("io.github.evaggelos99.ems.event-service.db.port", PG::getFirstMappedPort);
        registry.add("io.github.evaggelos99.ems.event-service.db.database", PG::getDatabaseName);
        registry.add("io.github.evaggelos99.ems.event-service.db.host", PG::getHost);
        registry.add("io.github.evaggelos99.ems.event-service.db.password", PG::getPassword);
    }

    @BeforeAll
    void beforeAll() {

        sqlScriptExecutor.setup("migration/pg-schema.sql");
    }

    @Test
    void consumeEventStreamingPrefixedTopics_miniLoadTest() {

        int count = 80_000;
        final UUID eventId = simulatePublishingMessages(count);
        final UUID eventId2 = simulatePublishingMessages(count);
        try {
            Thread.sleep(30_000);
        } catch (InterruptedException e) {
            fail(e);
        }

        StepVerifier.create(eventRepository.findAllEventStreams(eventId))
                .expectNextCount(count)
                .verifyComplete();

        StepVerifier.create(eventRepository.findAllEventStreams(eventId2))
                .expectNextCount(count)
                .verifyComplete();
    }

    private UUID simulatePublishingMessages(int count) {

        final var random = new Random();
        final UUID objectUid = UUID.randomUUID();

        String topic = topicPrefix + objectUid;
        kafkaAdmin.createOrModifyTopics(new NewTopic(topic, 1, (short) 1));

        IntStream.range(0, count)
                .forEach(num -> kafkaTemplate.send(topic, new EventStreamPayload(objectUid, "text", Instant.now(), "body", "blabla" + num,
                        "EN", random.nextBoolean(), "{}")));

        return objectUid;
    }
}