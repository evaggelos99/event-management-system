package io.github.evaggelos99.ems.event.service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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

import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.api.util.EventObjectGenerator;
import io.github.evaggelos99.ems.event.service.EventServiceApplication;
import io.github.evaggelos99.ems.event.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.event.service.util.TestConfiguration;

@SpringBootTest(classes = { EventServiceApplication.class,
		TestConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class EventControllerIntegrationTest {

	private static final String HOSTNAME = "http://localhost:";
	private static final String RELATIVE_ENDPOINT = "/event";
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
		assertEquals(actualDto, actualGetEntity.getBody());
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
		assertEquals(actualDto.createdAt(), actualPutDto.createdAt());
		assertTrue(actualPutDto.lastUpdated().isAfter(actualDto.lastUpdated()));
		assertEquals(updatedDto.organizerId(), actualPutDto.organizerId());
		// deleteEvent
		restTemplate.delete(createUrl() + "/" + actualDto.uuid());
		// assertThat the list returned is empty
		@SuppressWarnings("rawtypes")
		final ResponseEntity<List> listOfEventDtos = restTemplate.getForEntity(createUrl(), List.class);
		assertTrue(listOfEventDtos.getStatusCode().is2xxSuccessful());
		final List<?> body = listOfEventDtos.getBody();
		assertNotNull(body);
		assertTrue(body.isEmpty());
	}

	@SuppressWarnings({ "all" })
	private HttpEntity createHttpEntity(final EventDto updatedDto) {

		return new HttpEntity(updatedDto);
	}

	private String createUrl() {

		return HOSTNAME + port + RELATIVE_ENDPOINT;
	}

}
