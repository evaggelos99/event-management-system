package io.github.evaggelos99.ems.ticket.service;

import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import io.github.evaggelos99.ems.ticket.api.util.TicketObjectGenerator;
import io.github.evaggelos99.ems.ticket.service.util.SqlScriptExecutor;
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
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TicketServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
class TicketRepositoryTest {

    @Container
    static PostgreSQLContainer PG = new PostgreSQLContainer<>("postgres:16-alpine")
            .withUsername("event-management-system-user")
            .withPassword("event-management-system-user")
            .withDatabaseName("event-management-system")
            .withExposedPorts(5432);

    static {

        PG.setWaitStrategy(new LogMessageWaitStrategy().withRegEx(".*database system is ready to accept connections.*")
                .withStartupTimeout(Duration.of(15, ChronoUnit.SECONDS)));
        PG.start();
    }

    @AfterAll
    static void tearDown() {
        PG.stop();
    }

    private final AtomicInteger ai = new AtomicInteger(0);

    @Autowired
    private TicketRepository repository;
    @Autowired
    SqlScriptExecutor sqlScriptExecutor;

    @BeforeAll
    void beforeAll() {

        sqlScriptExecutor.setup("migration/pg-schema.sql");
    }

    @DynamicPropertySource
    static void configureDbProperties(DynamicPropertyRegistry registry) {

        registry.add("io.github.evaggelos99.ems.ticket-service.db.username", PG::getUsername);
        registry.add("io.github.evaggelos99.ems.ticket-service.db.port", PG::getFirstMappedPort);
        registry.add("io.github.evaggelos99.ems.ticket-service.db.database", PG::getDatabaseName);
        registry.add("io.github.evaggelos99.ems.ticket-service.db.host", PG::getHost);
        registry.add("io.github.evaggelos99.ems.ticket-service.db.password", PG::getPassword);
    }

    @Test
    void save() {

        final TicketDto expected = TicketObjectGenerator.generateTicketDto(null, UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(t -> assertTicketEquals(t, expected))
                .verifyComplete();
        ai.incrementAndGet();
    }

    @Test
    void edit() {

        final TicketDto expected = TicketObjectGenerator.generateTicketDto(null, UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(t -> assertTicketEquals(t, expected))
                .verifyComplete();
        ai.incrementAndGet();

        final TicketDto editedExpected = TicketObjectGenerator.generateTicketDto(expected.uuid(), UUID.randomUUID());

        StepVerifier.create(repository.edit(editedExpected))
                .assertNext(t -> assertTicketEquals(t, editedExpected))
                .verifyComplete();
    }

    @Test
    void deleteById() {
        final TicketDto expected = TicketObjectGenerator.generateTicketDto(null, UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(t -> assertTicketEquals(t, expected))
                .verifyComplete();

        StepVerifier.create(repository.deleteById(expected.uuid()))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void existsById_findById() {

        final TicketDto expected = TicketObjectGenerator.generateTicketDto(null, UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(t -> assertTicketEquals(t, expected))
                .verifyComplete();
        ai.incrementAndGet();

        StepVerifier.create(repository.existsById(expected.uuid()))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();

        StepVerifier.create(repository.findById(expected.uuid()))
                .assertNext(t -> assertTicketEquals(t, expected))
                .verifyComplete();
    }

    @Test
    void findAll() {

        StepVerifier.create(repository.findAll())
                .expectNextCount(ai.get())
                .verifyComplete();
    }


    private void assertTicketEquals(final Ticket t, final TicketDto expected) {
        assertEquals(t.getUuid(), expected.uuid());
        assertEquals(t.getEventID(), expected.eventID());
        assertEquals(t.getPrice(), expected.price());
        assertEquals(t.getUsed(), expected.used());
        assertEquals(t.getTransferable(), expected.transferable());
        assertEquals(t.getSeatingInformation(), expected.seatInformation());
    }
}