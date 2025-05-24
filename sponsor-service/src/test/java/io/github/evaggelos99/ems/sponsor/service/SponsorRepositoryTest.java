package io.github.evaggelos99.ems.sponsor.service;

import io.github.evaggelos99.ems.sponsor.api.Sponsor;
import io.github.evaggelos99.ems.sponsor.api.SponsorDto;
import io.github.evaggelos99.ems.sponsor.api.util.SponsorObjectGenerator;
import io.github.evaggelos99.ems.sponsor.service.util.SqlScriptExecutor;
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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {SponsorServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
class SponsorRepositoryTest {

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
    private SponsorRepository repository;
    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;

    @AfterAll
    static void tearDown() {

        PG.stop();
        PG.close();
    }

    @DynamicPropertySource
    static void configureKafkaProperties(DynamicPropertyRegistry registry) {

        registry.add("io.github.evaggelos99.ems.sponsor-service.db.username", PG::getUsername);
        registry.add("io.github.evaggelos99.ems.sponsor-service.db.port", PG::getFirstMappedPort);
        registry.add("io.github.evaggelos99.ems.sponsor-service.db.database", PG::getDatabaseName);
        registry.add("io.github.evaggelos99.ems.sponsor-service.db.host", PG::getHost);
        registry.add("io.github.evaggelos99.ems.sponsor-service.db.password", PG::getPassword);
    }

    @BeforeAll
    void beforeAll() {

        sqlScriptExecutor.setup("migration/pg-schema.sql");
    }

    @Test
    void save() {

        final SponsorDto expected = SponsorObjectGenerator.generateSponsorDto(null);

        StepVerifier.create(repository.save(expected))
                .assertNext(sp -> assertSponsorEquals(sp, expected))
                .verifyComplete();
        ai.incrementAndGet();
    }

    @Test
    void findById_existsById() {

        final SponsorDto expected = SponsorObjectGenerator.generateSponsorDto(null);

        StepVerifier.create(repository.save(expected))
                .assertNext(sp -> assertSponsorEquals(sp, expected))
                .verifyComplete();
        ai.incrementAndGet();

        StepVerifier.create(repository.findById(expected.uuid()))
                .assertNext(sp -> assertSponsorEquals(sp, expected))
                .verifyComplete();

        StepVerifier.create(repository.existsById(expected.uuid()))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void deleteById() {

        final SponsorDto expected = SponsorObjectGenerator.generateSponsorDto(null);

        StepVerifier.create(repository.save(expected))
                .assertNext(sp -> assertSponsorEquals(sp, expected))
                .verifyComplete();

        StepVerifier.create(repository.deleteById(expected.uuid()))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void findAll() {

        StepVerifier.create(repository.findAll())
                .expectNextCount(ai.get())
                .verifyComplete();
    }

    @Test
    void edit() {

        final SponsorDto expected = SponsorObjectGenerator.generateSponsorDto(null);

        StepVerifier.create(repository.save(expected))
                .assertNext(sp -> assertSponsorEquals(sp, expected))
                .verifyComplete();
        ai.incrementAndGet();

        SponsorDto editedExpected = SponsorObjectGenerator.generateSponsorDto(expected.uuid());

        StepVerifier.create(repository.edit(editedExpected))
                .assertNext(sp -> assertSponsorEquals(sp, editedExpected)).verifyComplete();
    }

    private void assertSponsorEquals(final Sponsor sp, final SponsorDto expected) {
        assertEquals(expected.uuid(), sp.getUuid());
        assertEquals(expected.name(), sp.getName());
        assertEquals(expected.financialContribution(), sp.getFinancialContribution());
        assertEquals(expected.website(), sp.getWebsite());
        assertEquals(expected.contactInformation(), sp.getContactInformation());
    }
}