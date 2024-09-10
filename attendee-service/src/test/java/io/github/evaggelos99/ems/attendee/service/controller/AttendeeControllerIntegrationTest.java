package io.github.evaggelos99.ems.attendee.service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import io.github.evaggelos99.ems.attendee.api.util.AttendeeObjectGenerator;
import io.github.evaggelos99.ems.attendee.service.AttendeeServiceApplication;
import io.github.evaggelos99.ems.attendee.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.attendee.service.util.TestConfiguration;

@SpringBootTest(classes = { AttendeeServiceApplication.class,
		TestConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class AttendeeControllerIntegrationTest {

	private static final String HOSTNAME = "http://localhost:";
	private static final String RELATIVE_ENDPOINT = "/attendee";
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
		final AttendeeDto dto = AttendeeObjectGenerator.generateAttendeeDtoWithoutTimestamps();
		// postAttendee
		final ResponseEntity<AttendeeDto> actualEntity = this.restTemplate.postForEntity(this.createUrl(), dto,
				AttendeeDto.class);
		assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
		final AttendeeDto actualDto = actualEntity.getBody();
		// assert
		assertNotNull(actualDto);
		assertEquals(dto.uuid(), actualDto.uuid());
		assertTrue(actualDto.createdAt().isAfter(currentTime));
		assertTrue(actualDto.lastUpdated().isAfter(currentTime));
		assertEquals(dto.firstName(), actualDto.firstName());
		assertEquals(dto.lastName(), actualDto.lastName());
		assertTrue(actualDto.ticketIDs().isEmpty());
		// getAttendee
		final ResponseEntity<AttendeeDto> actualGetEntity = this.restTemplate
				.getForEntity(this.createUrl() + "/" + actualDto.uuid(), AttendeeDto.class);
		// assert
		assertTrue(actualGetEntity.getStatusCode().is2xxSuccessful());
		final AttendeeDto getDto = actualGetEntity.getBody();
		assertNotNull(getDto);
		assertEquals(actualDto.uuid(), getDto.uuid());
		assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
				getDto.createdAt().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
		assertEquals(actualDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS),
				getDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
		assertEquals(actualDto.firstName(), getDto.firstName());
		assertEquals(actualDto.lastName(), getDto.lastName());
		assertEquals(actualDto.ticketIDs(), getDto.ticketIDs());

		// deleteAttendee
		final ResponseEntity<Void> deletedEntity = this.restTemplate.exchange(this.createUrl() + "/" + actualDto.uuid(),
				HttpMethod.DELETE, null, Void.class);
		assertTrue(deletedEntity.getStatusCode().is2xxSuccessful());
		// assertThat it cannot be found
		final ResponseEntity<AttendeeDto> deletedDto = this.restTemplate
				.getForEntity(this.createUrl() + "/" + actualDto.uuid(), AttendeeDto.class);
		assertTrue(deletedDto.getStatusCode().is2xxSuccessful());
		assertNull(deletedDto.getBody());
	}

	@Test
	void postAttendee_putAttendee_getAttendee_deleteAttendee_getAll_whenInvokedWithValidAttendeeDto_thenExpectForAttendeeToBeAddedThenEditedThenDeleted() {

		final Instant currentTime = Instant.now();
		final UUID ticketId = UUID.randomUUID();
		final AttendeeDto dto = AttendeeObjectGenerator.generateAttendeeDtoWithoutTimestamps(ticketId);
		// postAttendee
		final ResponseEntity<AttendeeDto> actualEntity = this.restTemplate.postForEntity(this.createUrl(), dto,
				AttendeeDto.class);
		assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
		final AttendeeDto actualDto = actualEntity.getBody();
		// assert
		assertNotNull(actualDto);
		assertEquals(dto.uuid(), actualDto.uuid());
		assertTrue(actualDto.createdAt().isAfter(currentTime));
		assertTrue(actualDto.lastUpdated().isAfter(currentTime));
		assertEquals(dto.firstName(), actualDto.firstName());
		assertEquals(dto.lastName(), actualDto.lastName());
		assertTrue(actualDto.ticketIDs().size() == 1);
		assertTrue(actualDto.ticketIDs().contains(ticketId));
		// putAttendee
		final UUID differentTicketId = UUID.randomUUID();
		final AttendeeDto updatedDto = new AttendeeDto(actualDto.uuid(), null, null, UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), List.of(differentTicketId));
		final ResponseEntity<AttendeeDto> actualPutEntity = this.restTemplate.exchange(
				this.createUrl() + "/" + actualDto.uuid(), HttpMethod.PUT, this.createHttpEntity(updatedDto),
				AttendeeDto.class);
		final AttendeeDto actualPutDto = actualPutEntity.getBody();
		// assert
		assertTrue(actualPutEntity.getStatusCode().is2xxSuccessful());
		assertNotNull(actualPutDto);
		assertEquals(actualPutDto.uuid(), actualDto.uuid());
		assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
				actualPutDto.createdAt().truncatedTo(ChronoUnit.MILLIS));
		assertTrue(actualPutDto.lastUpdated().isAfter(actualDto.lastUpdated()));
		assertEquals(updatedDto.firstName(), actualPutDto.firstName());
		assertEquals(updatedDto.lastName(), actualPutDto.lastName());
		assertTrue(updatedDto.ticketIDs().size() == 1);
		assertTrue(updatedDto.ticketIDs().contains(differentTicketId));
		// deleteAttendee
		this.restTemplate.delete(this.createUrl() + "/" + actualDto.uuid());
		// assertThat the list returned is empty
		@SuppressWarnings("rawtypes")
		final ResponseEntity<List> listOfAttendeeDtos = this.restTemplate.getForEntity(this.createUrl(), List.class);
		assertTrue(listOfAttendeeDtos.getStatusCode().is2xxSuccessful());
		final List<?> body = listOfAttendeeDtos.getBody();
		assertNotNull(body);
		assertTrue(body.isEmpty());
	}

	@SuppressWarnings({ "all" })
	private HttpEntity createHttpEntity(final AttendeeDto updatedDto) {

		return new HttpEntity(updatedDto);
	}

	private String createUrl() {

		return HOSTNAME + this.port + RELATIVE_ENDPOINT;
	}
}
