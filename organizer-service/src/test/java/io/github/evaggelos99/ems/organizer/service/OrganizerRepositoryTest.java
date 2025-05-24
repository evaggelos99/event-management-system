package io.github.evaggelos99.ems.organizer.service;

import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.github.evaggelos99.ems.organizer.api.OrganizerDto;
import io.github.evaggelos99.ems.organizer.api.util.OrganizerObjectGenerator;
import io.github.evaggelos99.ems.organizer.service.util.SqlScriptExecutor;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {OrganizerServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
class OrganizerRepositoryTest {

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
    private OrganizerRepository repository;
    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;

    @AfterAll
    static void tearDown() {

        PG.stop();
        PG.close();
    }

    @BeforeAll
    void beforeAll() {

        sqlScriptExecutor.setup("migration/pg-schema.sql");
    }

    @DynamicPropertySource
    static void configureDbProperties(DynamicPropertyRegistry registry) {

        registry.add("io.github.evaggelos99.ems.organizer-service.db.username", PG::getUsername);
        registry.add("io.github.evaggelos99.ems.organizer-service.db.port", PG::getFirstMappedPort);
        registry.add("io.github.evaggelos99.ems.organizer-service.db.database", PG::getDatabaseName);
        registry.add("io.github.evaggelos99.ems.organizer-service.db.host", PG::getHost);
        registry.add("io.github.evaggelos99.ems.organizer-service.db.password", PG::getPassword);
    }

    @Test
    void saveWithoutEventTypes() {

        final OrganizerDto expected = OrganizerObjectGenerator.generateOrganizerDto(null);

        StepVerifier.create(repository.save(expected))
                .assertNext(organizer -> assertOrganizerEquals(organizer, expected)).verifyComplete();

        ai.incrementAndGet();
    }

    @Test
    void saveWithEventTypes() {

        final OrganizerDto expected = OrganizerObjectGenerator.generateOrganizerDto(UUID.randomUUID(), EventType.OTHER, EventType.SPORT);

        StepVerifier.create(repository.save(expected))
                .assertNext(organizer -> {
                    assertOrganizerEquals(organizer, expected);
                    assertEquals(organizer.getUuid(), expected.uuid());
                    assertEquals(organizer.getEventTypes(), expected.eventTypes());
                }).verifyComplete();
        ai.incrementAndGet();
    }

    @Test
    void findById_existsById_Exists() {

        final OrganizerDto expected = OrganizerObjectGenerator.generateOrganizerDto(UUID.randomUUID(), EventType.SPORT, EventType.CONFERENCE, EventType.OTHER);

        StepVerifier.create(repository.save(expected))
                .assertNext(organizer -> assertOrganizerEquals(organizer, expected)).verifyComplete();
        ai.incrementAndGet();

        StepVerifier.create(repository.findById(expected.uuid()))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

        StepVerifier.create(repository.existsById(expected.uuid()))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void findById_existsById_DoesNotExists() {

        StepVerifier.create(repository.findById(UUID.randomUUID()))
                .verifyComplete();

        StepVerifier.create(repository.existsById(UUID.randomUUID()))
                .assertNext(Assertions::assertFalse)
                .verifyComplete();
    }

    @Test
    void deleteById() {

        final OrganizerDto expected = OrganizerObjectGenerator.generateOrganizerDto(UUID.randomUUID(), EventType.SPORT, EventType.CONFERENCE, EventType.OTHER);

        StepVerifier.create(repository.save(expected))
                .assertNext(organizer -> assertOrganizerEquals(organizer, expected)).verifyComplete();
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

        final OrganizerDto expected = OrganizerObjectGenerator.generateOrganizerDto(UUID.randomUUID());

        StepVerifier.create(repository.save(expected))
                .assertNext(organizer -> assertOrganizerEquals(organizer, expected)).verifyComplete();
        ai.incrementAndGet();

        final OrganizerDto editedExpected = OrganizerObjectGenerator.generateOrganizerDto(expected.uuid(), EventType.SPORT, EventType.CONFERENCE, EventType.OTHER);

        StepVerifier.create(repository.edit(editedExpected))
                .assertNext(organizer -> {
                    assertOrganizerEquals(organizer, editedExpected);
                    assertEquals(organizer.getEventTypes(), editedExpected.eventTypes());
                }).verifyComplete();
    }

    private static void assertOrganizerEquals(final Organizer actual, final OrganizerDto expected) {

        assertEquals(actual.getContactInformation(), expected.contactInformation());
        assertEquals(actual.getInformation(), expected.information());
        assertEquals(actual.getName(), expected.name());
        assertEquals(actual.getWebsite(), expected.website());
    }
}