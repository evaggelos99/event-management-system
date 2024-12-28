package io.github.evaggelos99.ems.organizer.service.controller;

import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.organizer.api.OrganizerDto;
import io.github.evaggelos99.ems.organizer.api.util.OrganizerObjectGenerator;
import io.github.evaggelos99.ems.organizer.service.OrganizerServiceApplication;
import io.github.evaggelos99.ems.organizer.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.organizer.service.util.TestConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {OrganizerServiceApplication.class,
        TestConfiguration.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class OrganizerControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost:";
    private static final String RELATIVE_ENDPOINT = "/organizer";
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;
    @LocalServerPort
    private int port;

    @BeforeAll
    void beforeAll() {

        sqlScriptExecutor.setup();
    }

    @Test
    void postOrganizer_getOrganizer_deleteOrganizer_getOrganizer_whenInvokedWithValidOrganizerDto_thenExpectForOrganizerToBeAddedFetchedAndDeleted() {

        final Instant currentTime = Instant.now();
        final OrganizerDto dto = OrganizerObjectGenerator.generateOrganizerDtoWithoutTimestamps();
        // postOrganizer
        final ResponseEntity<OrganizerDto> actualEntity = restTemplate.postForEntity(createUrl(), dto,
                OrganizerDto.class);
        assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
        final OrganizerDto actualDto = actualEntity.getBody();
        // assert
        assertNotNull(actualDto);
        assertEquals(dto.uuid(), actualDto.uuid());
        assertTrue(actualDto.createdAt().isAfter(currentTime));
        assertTrue(actualDto.lastUpdated().isAfter(currentTime));
        assertEquals(dto.name(), actualDto.name());
        assertEquals(dto.website(), actualDto.website());
        assertEquals(dto.information(), actualDto.information());
        assertEquals(dto.contactInformation(), actualDto.contactInformation());
        assertTrue(actualDto.eventTypes().isEmpty());

        // getOrganizer
        final ResponseEntity<OrganizerDto> actualGetEntity = restTemplate
                .getForEntity(createUrl() + "/" + actualDto.uuid(), OrganizerDto.class);
        // assert
        assertTrue(actualGetEntity.getStatusCode().is2xxSuccessful());
        final OrganizerDto getDto = actualGetEntity.getBody();
        assertNotNull(getDto);
        assertEquals(actualDto.uuid(), getDto.uuid());
        assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
                getDto.createdAt().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
        assertEquals(actualDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS),
                getDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
        assertEquals(actualDto.name(), getDto.name());
        assertEquals(actualDto.website(), getDto.website());
        assertEquals(actualDto.information(), getDto.information());
        assertEquals(actualDto.eventTypes(), getDto.eventTypes());
        assertEquals(actualDto.contactInformation(), getDto.contactInformation());

        // deleteOrganizer
        final ResponseEntity<Void> deletedEntity = restTemplate.exchange(createUrl() + "/" + actualDto.uuid(),
                HttpMethod.DELETE, null, Void.class);
        assertTrue(deletedEntity.getStatusCode().is2xxSuccessful());
        // assertThat it cannot be found
        final ResponseEntity<OrganizerDto> deletedDto = restTemplate.getForEntity(createUrl() + "/" + actualDto.uuid(),
                OrganizerDto.class);
        assertTrue(deletedDto.getStatusCode().is2xxSuccessful());
        assertNull(deletedDto.getBody());
    }

    @Test
    void postOrganizer_putOrganizer_getOrganizer_deleteOrganizer_getAll_whenInvokedWithValidOrganizerDto_thenExpectForOrganizerToBeAddedThenEditedThenDeleted() {

        final Instant currentTime = Instant.now();
        final OrganizerDto dto = OrganizerObjectGenerator.generateOrganizerDtoWithoutTimestamps(EventType.OTHER);
        // postOrganizer
        final ResponseEntity<OrganizerDto> actualEntity = restTemplate.postForEntity(createUrl(), dto,
                OrganizerDto.class);
        assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
        final OrganizerDto actualDto = actualEntity.getBody();
        // assert
        assertNotNull(actualDto);
        assertEquals(dto.uuid(), actualDto.uuid());
        assertTrue(actualDto.createdAt().isAfter(currentTime));
        assertTrue(actualDto.lastUpdated().isAfter(currentTime));
        assertEquals(dto.name(), actualDto.name());
        assertEquals(dto.website(), actualDto.website());
        assertEquals(dto.information(), actualDto.information());
        assertEquals(dto.contactInformation(), actualDto.contactInformation());
        assertFalse(actualDto.eventTypes().isEmpty());
        assertEquals(dto.eventTypes(), actualDto.eventTypes());

        // putOrganizer
        final List<EventType> eventTypes = List.of(EventType.OTHER, EventType.CONFERENCE);
        final ContactInformation contactInformation = OrganizerObjectGenerator.generateContactInformation();

        final OrganizerDto updatedDto = new OrganizerDto(actualDto.uuid(), null, null, UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), eventTypes, contactInformation);

        final ResponseEntity<OrganizerDto> actualPutEntity = restTemplate.exchange(createUrl() + "/" + actualDto.uuid(),
                HttpMethod.PUT, createHttpEntity(updatedDto), OrganizerDto.class);
        final OrganizerDto actualPutDto = actualPutEntity.getBody();
        // assert
        assertTrue(actualPutEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(actualPutDto);
        assertEquals(updatedDto.uuid(), actualPutDto.uuid());
        assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
                actualPutDto.createdAt().truncatedTo(ChronoUnit.MILLIS));
        assertTrue(actualPutDto.lastUpdated().isAfter(actualDto.lastUpdated()));
        assertEquals(updatedDto.name(), actualPutDto.name());
        assertEquals(updatedDto.website(), actualPutDto.website());
        assertEquals(updatedDto.information(), actualPutDto.information());
        assertEquals(updatedDto.contactInformation(), actualPutDto.contactInformation());
        assertEquals(eventTypes, actualPutDto.eventTypes());

        // deleteOrganizer
        restTemplate.delete(createUrl() + "/" + actualDto.uuid());
        // assertThat the list returned is empty
        @SuppressWarnings("rawtypes") final ResponseEntity<List> listOfOrganizers = restTemplate.getForEntity(createUrl(), List.class);
        assertTrue(listOfOrganizers.getStatusCode().is2xxSuccessful());
        final List<?> body = listOfOrganizers.getBody();
        assertNotNull(body);
        assertTrue(body.isEmpty());
    }

    @SuppressWarnings({"all"})
    private HttpEntity createHttpEntity(final OrganizerDto updatedDto) {

        return new HttpEntity(updatedDto);
    }

    private String createUrl() {

        return HOSTNAME + port + RELATIVE_ENDPOINT;
    }
}
