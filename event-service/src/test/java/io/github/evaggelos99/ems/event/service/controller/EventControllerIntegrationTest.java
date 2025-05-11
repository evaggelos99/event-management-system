package io.github.evaggelos99.ems.event.service.controller;

import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.api.util.EventObjectGenerator;
import io.github.evaggelos99.ems.event.service.EventServiceApplication;
import io.github.evaggelos99.ems.event.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.event.service.util.TestConfiguration;
import io.github.evaggelos99.ems.testcontainerkafka.lib.ExtendedKafkaContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {EventServiceApplication.class,
        TestConfiguration.class,}, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@Testcontainers
public class EventControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost:";
    private static final String RELATIVE_ENDPOINT = "/event";
    @Container
    private static final ExtendedKafkaContainer KAFKA = new ExtendedKafkaContainer();
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;
    @LocalServerPort
    private int port;

    EventControllerIntegrationTest() {
        KAFKA.start();

        System.setProperty("spring.kafka.consumer.bootstrap-servers", KAFKA.getBootstrapServers());
    }

    @BeforeAll
    public void beforeAll() {

        sqlScriptExecutor.setup();
    }


    @Test
    @WithMockUser(roles = {"CREATE_EVENT", "UPDATE_EVENT", "DELETE_EVENT", "READ_EVENT"})
    void postEvent_getEvent_deleteEvent_getEvent_whenInvokedWithValidEventDto_thenExpectForEventToBeAddedFetchedAndDeleted() {

        final OffsetDateTime currentTime = OffsetDateTime.now();

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
        restTemplate.delete(createUrl() + "/{eventId}", actualDto.uuid());
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
    @WithMockUser(roles = {"CREATE_EVENT", "UPDATE_EVENT", "DELETE_EVENT", "READ_EVENT"})
    void postEvent_putEvent_getEvent_deleteEvent_getAll_whenInvokedWithValidEventDto_thenExpectForEventToBeAddedThenEditedThenDeleted() {

        final OffsetDateTime currentTime = OffsetDateTime.now();

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

    @Test
    @WithMockUser(roles = {"CREATE_EVENT", "UPDATE_EVENT", "DELETE_EVENT", "READ_EVENT"})
    void postEvent_addAttendee_deleteEvent_whenInvokedWithValidEventDto_thenExpectForEventToBeAddedThenEditedWithAddAttendeeThenDeleted() {

        final OffsetDateTime currentTime = OffsetDateTime.now();

        final UUID attendeeId = UUID.randomUUID();
        final UUID organizerId = UUID.randomUUID();
        final UUID sponsorId = UUID.randomUUID();

        final EventDto dto = EventObjectGenerator.generateEventDtoWithoutTimestamps(null, organizerId, sponsorId);
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
        assertTrue(actualDto.attendeesIds().isEmpty());
        assertEquals(organizerId, actualDto.organizerId());
        assertEquals(dto.limitOfPeople(), actualDto.limitOfPeople());
        assertTrue(actualDto.sponsorsIds().contains(sponsorId));
        assertEquals(dto.startTimeOfEvent(), actualDto.startTimeOfEvent());
        assertEquals(dto.duration(), actualDto.duration());

        final ResponseEntity<Boolean> successfulOperation = restTemplate.exchange(createUrl() + "/{eventId}/addAttendee?attendeeId={attendeeId}", HttpMethod.PUT, null, Boolean.class, actualDto.uuid(), attendeeId);

        assertTrue(successfulOperation.getStatusCode().is2xxSuccessful());
        assertEquals(Boolean.TRUE, successfulOperation.getBody());

        restTemplate.delete(createUrl() + "/{eventId}", actualDto.uuid());
    }

    @Test
    @WithMockUser(roles = {"READ_EVENT"})
    void postEventWithWrongRole() {

        final EventDto dto = EventObjectGenerator.generateEventDtoWithoutTimestamps(UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID());
        try {
            restTemplate.postForEntity(createUrl(), dto, EventDto.class);
        } catch (HttpClientErrorException.Forbidden e) {
            return;
        }
        throw new AssertionError("The request status is not 403");
    }

    @Test
    void postEventWithNoRole() {

        final EventDto dto = EventObjectGenerator.generateEventDto(UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID());
        try {
            restTemplate.postForEntity(createUrl(), dto, EventDto.class);
        } catch (HttpClientErrorException.Unauthorized e) {
            return;
        }
        throw new AssertionError("The request status is not 401");
    }

}
