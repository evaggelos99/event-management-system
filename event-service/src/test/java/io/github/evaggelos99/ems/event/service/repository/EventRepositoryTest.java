package io.github.evaggelos99.ems.event.service.repository;

import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.api.util.EventObjectGenerator;
import io.github.evaggelos99.ems.event.service.EventServiceApplication;
import io.github.evaggelos99.ems.event.service.util.SqlScriptExecutor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {EventServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
class EventRepositoryTest {

    @Container
    static PostgreSQLContainer<?> PG = new PostgreSQLContainer<>("postgres:16-alpine")
            .withUsername("event-management-system-user")
            .withPassword("event-management-system-user")
            .withDatabaseName("event-management-system")
            .withExposedPorts(5432)
            .waitingFor(new LogMessageWaitStrategy().withRegEx(".*database system is ready to accept connections.*")
                    .withStartupTimeout(Duration.of(30, ChronoUnit.SECONDS)));

    static {

        PG.start();
    }
    private final AtomicInteger ai = new AtomicInteger(0);
    @Autowired
    private EventRepository repository;
    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;

    @AfterAll
    static void tearDown() {

        PG.stop();
        PG.close();
    }

    @DynamicPropertySource
    static void configureKafkaProperties(DynamicPropertyRegistry registry) {

        registry.add("kafka.enabled", () -> false);
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
    void save() {

        final UUID eventId = UUID.randomUUID();
        final UUID organizerId = UUID.randomUUID();

        final EventDto expected = EventObjectGenerator.generateEventDto(eventId, null, organizerId, null);

        StepVerifier.create(repository.save(expected))
                .assertNext(ev -> assertEventEquals(ev, expected)).verifyComplete();
        ai.incrementAndGet();
    }

    @Test
    void saveWithAttendee() {

        final EventDto expected = EventObjectGenerator.generateEventDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(ev -> assertEventEquals(ev, expected)).verifyComplete();
        ai.incrementAndGet();
    }

    @Test
    void findById_existsById_exists() {

        final EventDto expected = EventObjectGenerator.generateEventDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(ev -> assertEventEquals(ev, expected)).verifyComplete();
        ai.incrementAndGet();

        StepVerifier.create(repository.findById(expected.uuid()))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

        StepVerifier.create(repository.existsById(expected.uuid()))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void findById_existsById_doesNotExists() {

        StepVerifier.create(repository.findById(UUID.randomUUID()))
                .verifyComplete();

        StepVerifier.create(repository.existsById(UUID.randomUUID()))
                .assertNext(Assertions::assertFalse)
                .verifyComplete();
    }

    @Test
    void findAll() {

        StepVerifier.create(repository.findAll()).expectNextCount(ai.get()).verifyComplete();
    }

    @Test
    void edit() {

        final EventDto expected = EventObjectGenerator.generateEventDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(att -> assertEventEquals(att, expected)).verifyComplete();
        ai.incrementAndGet();

        final List<UUID> sponsorsIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        final EventDto editedExpected = EventDto.builder()
                .from(EventObjectGenerator.generateEventDto(expected.uuid(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()))
                .sponsorsIds(sponsorsIds).build();

        StepVerifier.create(repository.edit(editedExpected))
                .assertNext(att -> assertEventEquals(att, editedExpected)).verifyComplete();
    }

    @Test
    void deleteByIdDoesNotExist() {

        final EventDto expected = EventObjectGenerator.generateEventDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(ev -> assertEventEquals(ev, expected)).verifyComplete();
        ai.incrementAndGet();

        StepVerifier.create(repository.deleteById(UUID.randomUUID()))
                .assertNext(Assertions::assertFalse)
                .verifyComplete();
    }

    @Test
    void deleteById() {

        StepVerifier.create(repository.deleteById(UUID.randomUUID()))
                .assertNext(Assertions::assertFalse)
                .verifyComplete();
    }

    @Test
    void saveOneEventStreamPayload() {

        EventDto expected = EventObjectGenerator.generateEventDto(UUID.randomUUID(), null, UUID.randomUUID(), null);

        StepVerifier.create(repository.save(expected))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();
        ai.incrementAndGet();

        EventStreamPayload payload = new EventStreamPayload(expected.uuid(), "text", Instant.now(), "ALERT", "Welcome to the party PAL!", "CN", false, "");
        StepVerifier.create(repository.saveOneEventStreamPayload(
                        payload))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();
    }

    @Test
    void saveMultipleEventStreamPayload_findAllEventStreams() {

        EventDto expected = EventObjectGenerator.generateEventDto(UUID.randomUUID(), null, UUID.randomUUID(), null);

        StepVerifier.create(repository.save(expected))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();
        ai.incrementAndGet();

        EventStreamPayload payloadOne = new EventStreamPayload(expected.uuid(), "text", Instant.now(), "ALERT", "Welcome to the party PAL!", "CN", false, "{\"twoone\" : \"onetwo\",\"hey2\" : [\"test\", \"test2\"]}");
        EventStreamPayload payloadTwo = new EventStreamPayload(expected.uuid(), "text", Instant.now(), "ANNOUNCEMENT", "TESTING", "GR", true, null);
        EventStreamPayload payloadThree = new EventStreamPayload(expected.uuid(), "text", Instant.now(), "ALERT", "DOB DOB YES YES", "EN", false, "{\"abc\" : \"efg\",\"hey\" : [\"test\", \"test2\"]}");

        StepVerifier.create(repository.saveMultipleEventStreamPayload(List.of(payloadOne, payloadTwo, payloadThree)))
                .verifyComplete();

        StepVerifier.create(repository.findAllEventStreams(expected.uuid()))
                .expectNextCount(3)
                .verifyComplete();
    }


    private void assertEventEquals(final Event ev, final EventDto expected) {

        assertEquals(expected.duration(), ev.getDuration());
        assertEquals(expected.eventType(), ev.getEventType());
        assertEquals(expected.organizerId(), ev.getOrganizerID());
        assertEquals(expected.name(), ev.getName());
        assertEquals(expected.place(), ev.getPlace());
        assertEquals(expected.limitOfPeople(), ev.getLimitOfPeople());
        assertEquals(expected.streamable(), ev.isStreamable());
        assertEquals(expected.attendeesIds(), ev.getAttendeesIDs());
        assertEquals(expected.sponsorsIds(), ev.getSponsorsIds());
    }
}