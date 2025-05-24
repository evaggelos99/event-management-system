package io.github.evaggelos99.ems.attendee.service.repository;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import io.github.evaggelos99.ems.attendee.api.util.AttendeeObjectGenerator;
import io.github.evaggelos99.ems.attendee.service.AttendeeServiceApplication;
import io.github.evaggelos99.ems.attendee.service.util.SqlScriptExecutor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {AttendeeServiceApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
class AttendeeRepositoryTest {

    @Container
    static PostgreSQLContainer<?> PG = new PostgreSQLContainer<>("postgres:16-alpine")
            .withUsername("event-management-system-user")
            .withPassword("event-management-system-user")
            .withDatabaseName("event-management-system")

            .waitingFor(new LogMessageWaitStrategy().withRegEx(".*database system is ready to accept connections.*")
                    .withStartupTimeout(Duration.of(30, ChronoUnit.SECONDS)));

    static {

        PG.start();
    }

    @AfterAll
    static void tearDown() {

        PG.stop();
        PG.close();
    }

    private final AtomicInteger ai = new AtomicInteger(0);

    @Autowired
    private AttendeeRepository repository;

    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;

    @BeforeAll
    void beforeAll() {

        sqlScriptExecutor.setup("migration/pg-schema.sql");
    }

    @DynamicPropertySource
    static void configureKafkaProperties(DynamicPropertyRegistry registry) {

        registry.add("kafka.enabled", () -> false);
        registry.add("io.github.evaggelos99.ems.attendee-service.db.username", PG::getUsername);
        registry.add("io.github.evaggelos99.ems.attendee-service.db.port", PG::getFirstMappedPort);
        registry.add("io.github.evaggelos99.ems.attendee-service.db.database", PG::getDatabaseName);
        registry.add("io.github.evaggelos99.ems.attendee-service.db.host", PG::getHost);
        registry.add("io.github.evaggelos99.ems.attendee-service.db.password", PG::getPassword);
    }

    @Test
    void saveWithoutTicketIds() {

        final AttendeeDto expected = AttendeeObjectGenerator.generateAttendeeDto(null);

        StepVerifier.create(repository.save(expected))
                .assertNext(att -> assertAttendeeEquals(att, expected)).verifyComplete();

        ai.incrementAndGet();
    }

    @Test
    void saveWithTicketIds() {

        final AttendeeDto expected = AttendeeObjectGenerator.generateAttendeeDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(att -> {
                    assertAttendeeEquals(att, expected);
                    assertEquals(att.getUuid(), expected.uuid());
                    assertEquals(att.getTicketIDs(), expected.ticketIDs());
                }).verifyComplete();

        ai.incrementAndGet();
    }

    @Test
    void findById_existsById_exists() {

        final AttendeeDto expected = AttendeeObjectGenerator.generateAttendeeDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(att -> assertAttendeeEquals(att, expected)).verifyComplete();
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
    void deleteById() {

        final AttendeeDto expected = AttendeeObjectGenerator.generateAttendeeDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(att -> assertAttendeeEquals(att, expected)).verifyComplete();
        ai.incrementAndGet();

        StepVerifier.create(repository.deleteById(expected.uuid()))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void deleteByIdDoesNotExists() {

        StepVerifier.create(repository.deleteById(UUID.randomUUID()))
                .assertNext(Assertions::assertFalse)
                .verifyComplete();
    }

    @Test
    void findAll() {

        StepVerifier.create(repository.findAll()).expectNextCount(ai.get()).verifyComplete();
    }

    @Test
    void edit() {

        final AttendeeDto expected = AttendeeObjectGenerator.generateAttendeeDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(att -> assertAttendeeEquals(att, expected)).verifyComplete();
        ai.incrementAndGet();

        final AttendeeDto editedExpected = AttendeeObjectGenerator.generateAttendeeDto(expected.uuid(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        StepVerifier.create(repository.edit(editedExpected))
                .assertNext(att -> assertAttendeeEquals(att, editedExpected)).verifyComplete();
    }

    private static void assertAttendeeEquals(final Attendee actual, final AttendeeDto expected) {

        assertEquals(expected.firstName(), actual.getFirstName());
        assertEquals(expected.lastName(), actual.getLastName());
        assertEquals(expected.ticketIDs(), actual.getTicketIDs());
    }
}