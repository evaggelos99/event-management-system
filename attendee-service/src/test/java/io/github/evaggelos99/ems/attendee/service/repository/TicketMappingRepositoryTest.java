package io.github.evaggelos99.ems.attendee.service.repository;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.util.AttendeeObjectGenerator;
import io.github.evaggelos99.ems.attendee.service.AttendeeServiceApplication;
import io.github.evaggelos99.ems.attendee.service.util.SqlScriptExecutor;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {AttendeeServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
class TicketMappingRepositoryTest {

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
    @Autowired
    private AttendeeRepository attendeeRepository;
    @Autowired
    private TicketMappingRepository repository;
    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;
    private Attendee current;

    @AfterAll
    static void tearDown() {

        PG.close();
    }

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

    @BeforeEach
    void setUp() {

        current = attendeeRepository.save(AttendeeObjectGenerator.generateAttendeeDto(null)).block();
    }

    @AfterEach
    void cleanUp() {

        attendeeRepository.deleteById(current.getUuid()).block();
    }

    @Test
    void saveMapping() {

        final UUID[] ticketIds = {UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()};
        StepVerifier.create(repository.saveMapping(current.getUuid(), ticketIds))
                .verifyComplete();

        StepVerifier.create(attendeeRepository.findById(current.getUuid()))
                .assertNext(x -> Assertions.assertEquals(x.getTicketIDs(), List.of(ticketIds)))
                .verifyComplete();
    }

    @Test
    void saveSingularMapping() {

        final UUID ticketId = UUID.randomUUID();
        StepVerifier.create(repository.saveSingularMapping(current.getUuid(), ticketId))
                .assertNext(x -> assertEquals(ticketId, x.ticketId()))
                .verifyComplete();

        StepVerifier.create(attendeeRepository.findById(current.getUuid()))
                .assertNext(x -> Assertions.assertEquals(x.getTicketIDs(), List.of(ticketId)))
                .verifyComplete();
    }

    @Test
    void editMapping() {

        final UUID[] ticketIds = {UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()};
        StepVerifier.create(repository.saveMapping(current.getUuid(), ticketIds))
                .verifyComplete();

        final UUID[] updatedTicketIds = {UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()};

        StepVerifier.create(repository.editMapping(current.getUuid(), updatedTicketIds))
                .verifyComplete();

        StepVerifier.create(attendeeRepository.findById(current.getUuid()))
                .assertNext(att -> assertEquals(List.of(updatedTicketIds), att.getTicketIDs()))
                .verifyComplete();
    }

    @Test
    void deleteMapping() {

        final UUID[] ticketIds = {UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()};

        StepVerifier.create(repository.saveMapping(current.getUuid(), ticketIds))
                .verifyComplete();

        StepVerifier.create(repository.deleteMapping(current.getUuid()))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();

        StepVerifier.create(attendeeRepository.findById(current.getUuid()))
                .assertNext(att -> assertTrue(att.getTicketIDs().isEmpty()))
                .verifyComplete();
    }

    @Test
    void deleteSingularMapping() {

        final UUID singleTicket = UUID.randomUUID();

        final UUID[] ticketIds = {singleTicket, UUID.randomUUID(), UUID.randomUUID()};

        StepVerifier.create(repository.saveMapping(current.getUuid(), ticketIds))
                .verifyComplete();

        StepVerifier.create(repository.deleteSingularMapping(current.getUuid(), singleTicket))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();

        StepVerifier.create(attendeeRepository.findById(current.getUuid()))
                .assertNext(att -> assertFalse(att.getTicketIDs().contains(singleTicket)))
                .verifyComplete();
    }
}