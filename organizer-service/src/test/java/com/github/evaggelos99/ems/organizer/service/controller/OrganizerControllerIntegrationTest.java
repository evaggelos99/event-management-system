package com.github.evaggelos99.ems.organizer.service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import com.github.evaggelos99.ems.common.api.domainobjects.EventType;
import com.github.evaggelos99.ems.organizer.api.OrganizerDto;
import com.github.evaggelos99.ems.organizer.api.util.OrganizerObjectGenerator;
import com.github.evaggelos99.ems.organizer.service.OrganizerServiceApplication;
import com.github.evaggelos99.ems.organizer.service.util.SqlScriptExecutor;
import com.github.evaggelos99.ems.organizer.service.util.TestConfiguration;

@SpringBootTest(classes = { OrganizerServiceApplication.class,
		TestConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
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

		this.sqlScriptExecutor.setup();
	}

	@Test
	void postAttendee_getAttendee_deleteAttendee_getAttendee_whenInvokedWithValidAttendeeDto_thenExpectForAttendeeToBeAddedFetchedAndDeleted() {

		final Instant currentTime = Instant.now();
		final OrganizerDto dto = OrganizerObjectGenerator.generateOrganizerDtoWithoutTimestamps();
		// postAttendee
		final ResponseEntity<OrganizerDto> actualEntity = restTemplate.postForEntity(this.createUrl(), dto,
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

		// getAttendee
		final ResponseEntity<OrganizerDto> actualGetEntity = restTemplate
				.getForEntity(this.createUrl() + "/" + actualDto.uuid(), OrganizerDto.class);
		// assert
		assertTrue(actualGetEntity.getStatusCode().is2xxSuccessful());
		assertEquals(actualDto, actualGetEntity.getBody());
		// deleteAttendee
		final ResponseEntity<Void> deletedEntity = restTemplate.exchange(this.createUrl() + "/" + actualDto.uuid(),
				HttpMethod.DELETE, null, Void.class);
		assertTrue(deletedEntity.getStatusCode().is2xxSuccessful());
		// assertThat it cannot be found
		final ResponseEntity<OrganizerDto> deletedDto = restTemplate
				.getForEntity(this.createUrl() + "/" + actualDto.uuid(), OrganizerDto.class);
		assertTrue(deletedDto.getStatusCode().is2xxSuccessful());
		assertNull(deletedDto.getBody());
	}

	@Test
	void postAttendee_putAttendee_getAttendee_deleteAttendee_getAll_whenInvokedWithValidAttendeeDto_thenExpectForAttendeeToBeAddedThenEditedThenDeleted() {

		final Instant currentTime = Instant.now();
		final OrganizerDto dto = OrganizerObjectGenerator.generateOrganizerDtoWithoutTimestamps(EventType.OTHER);
		// postAttendee
		final ResponseEntity<OrganizerDto> actualEntity = restTemplate.postForEntity(this.createUrl(), dto,
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

		// putAttendee
		final List<EventType> eventTypes = List.of(EventType.OTHER, EventType.CONFERENCE);
		final ContactInformation contactInformation = OrganizerObjectGenerator.generateContactInformation();

		final OrganizerDto updatedDto = new OrganizerDto(actualDto.uuid(), null, null, UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), UUID.randomUUID().toString(), eventTypes, contactInformation);

		final ResponseEntity<OrganizerDto> actualPutEntity = restTemplate.exchange(
				this.createUrl() + "/" + actualDto.uuid(), HttpMethod.PUT, this.createHttpEntity(updatedDto),
				OrganizerDto.class);
		final OrganizerDto actualPutDto = actualPutEntity.getBody();
		// assert
		assertTrue(actualPutEntity.getStatusCode().is2xxSuccessful());
		assertNotNull(actualPutDto);
		assertEquals(updatedDto.uuid(), actualPutDto.uuid());
		assertEquals(actualDto.createdAt(), actualPutDto.createdAt());
		assertTrue(actualPutDto.lastUpdated().isAfter(actualDto.lastUpdated()));
		assertEquals(updatedDto.name(), actualPutDto.name());
		assertEquals(updatedDto.website(), actualPutDto.website());
		assertEquals(updatedDto.information(), actualPutDto.information());
		assertEquals(updatedDto.contactInformation(), actualPutDto.contactInformation());
		assertEquals(eventTypes, actualPutDto.eventTypes());

		// deleteAttendee
		restTemplate.delete(this.createUrl() + "/" + actualDto.uuid());
		// assertThat the list returned is empty
		@SuppressWarnings("rawtypes")
		final ResponseEntity<List> listOfOrganizers = restTemplate.getForEntity(this.createUrl(), List.class);
		assertTrue(listOfOrganizers.getStatusCode().is2xxSuccessful());
		final List<?> body = listOfOrganizers.getBody();
		assertNotNull(body);
		assertTrue(body.isEmpty());
	}

	@SuppressWarnings({ "all" })
	private HttpEntity createHttpEntity(final OrganizerDto updatedDto) {

		return new HttpEntity(updatedDto);
	}

	private String createUrl() {

		return HOSTNAME + this.port + RELATIVE_ENDPOINT;
	}
}
