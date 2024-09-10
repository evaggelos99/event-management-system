package io.github.evaggelos99.ems.ticket.service.controller;

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

import io.github.evaggelos99.ems.ticket.api.TicketDto;
import io.github.evaggelos99.ems.ticket.api.util.TicketObjectGenerator;
import io.github.evaggelos99.ems.ticket.service.TicketServiceApplication;
import io.github.evaggelos99.ems.ticket.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.ticket.service.util.TestConfiguration;

@SpringBootTest(classes = { TicketServiceApplication.class,
		TestConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class TicketControllerIntegrationTest {

	private static final String HOSTNAME = "http://localhost:";
	private static final String RELATIVE_ENDPOINT = "/ticket";

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
	void postTicket_getTicket_deleteTicket_getTicket_whenInvokedWithValidTicketDto_thenExpectForTicketToBeAddedFetchedAndDeleted() {

		final Instant currentTime = Instant.now();
		final UUID eventId = UUID.randomUUID();
		final TicketDto dto = TicketObjectGenerator.generateTicketDtoWithoutTimestamps(null, eventId);

		final ResponseEntity<TicketDto> actualEntity = restTemplate.postForEntity(createUrl(), dto, TicketDto.class);
		assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
		final TicketDto actualDto = actualEntity.getBody();

		assertNotNull(actualDto);
		assertEquals(dto.uuid(), actualDto.uuid());
		assertTrue(actualDto.createdAt().isAfter(currentTime));
		assertTrue(actualDto.lastUpdated().isAfter(currentTime));
		assertEquals(dto.eventID(), actualDto.eventID());
		assertEquals(dto.ticketType(), actualDto.ticketType());
		assertEquals(dto.price(), actualDto.price());
		assertEquals(dto.transferable(), actualDto.transferable());
		assertEquals(dto.seatInformation(), actualDto.seatInformation());

		final ResponseEntity<TicketDto> actualGetEntity = restTemplate
				.getForEntity(createUrl() + "/" + actualDto.uuid(), TicketDto.class);

		assertTrue(actualGetEntity.getStatusCode().is2xxSuccessful());
		assertEquals(actualDto, actualGetEntity.getBody());

		final ResponseEntity<Void> deletedEntity = restTemplate.exchange(createUrl() + "/" + actualDto.uuid(),
				HttpMethod.DELETE, null, Void.class);
		assertTrue(deletedEntity.getStatusCode().is2xxSuccessful());

		final ResponseEntity<TicketDto> deletedDto = restTemplate.getForEntity(createUrl() + "/" + actualDto.uuid(),
				TicketDto.class);
		assertTrue(deletedDto.getStatusCode().is2xxSuccessful());
		assertNull(deletedDto.getBody());
	}

	@Test
	void postTicket_putTicket_deleteTicket_getAll_whenInvokedWithValidTicketDto_thenExpectForTicketToBeAddedThenEditedThenDeleted() {

		final Instant currentTime = Instant.now();
		final UUID eventId = UUID.randomUUID();
		final UUID ticketId = UUID.randomUUID();
		final TicketDto dto = TicketObjectGenerator.generateTicketDtoWithoutTimestamps(ticketId, eventId);

		final ResponseEntity<TicketDto> actualEntity = restTemplate.postForEntity(createUrl(), dto, TicketDto.class);
		assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
		final TicketDto actualDto = actualEntity.getBody();

		assertNotNull(actualDto);
		assertEquals(dto.uuid(), actualDto.uuid());
		assertTrue(actualDto.createdAt().isAfter(currentTime));
		assertTrue(actualDto.lastUpdated().isAfter(currentTime));
		assertEquals(dto.eventID(), actualDto.eventID());
		assertEquals(dto.ticketType(), actualDto.ticketType());
		assertEquals(dto.price(), actualDto.price());
		assertEquals(dto.transferable(), actualDto.transferable());
		assertEquals(dto.seatInformation(), actualDto.seatInformation());

		final TicketDto updatedDto = TicketObjectGenerator.generateTicketDtoWithoutTimestamps(ticketId, eventId);

		final ResponseEntity<TicketDto> actualPutEntity = restTemplate.exchange(createUrl() + "/" + actualDto.uuid(),
				HttpMethod.PUT, createHttpEntity(updatedDto), TicketDto.class);
		final TicketDto actualPutDto = actualPutEntity.getBody();
		// assert
		assertTrue(actualPutEntity.getStatusCode().is2xxSuccessful());
		assertNotNull(actualPutDto);
		assertEquals(updatedDto.uuid(), actualPutDto.uuid());
		assertEquals(actualDto.createdAt(), actualPutDto.createdAt());
		assertTrue(actualPutDto.lastUpdated().isAfter(actualDto.lastUpdated()));
		assertEquals(updatedDto.eventID(), actualPutDto.eventID());
		assertEquals(updatedDto.ticketType(), actualPutDto.ticketType());
		assertEquals(updatedDto.price(), actualPutDto.price());
		assertEquals(updatedDto.transferable(), actualPutDto.transferable());
		assertEquals(updatedDto.seatInformation(), actualPutDto.seatInformation());

		// deleteTicket
		restTemplate.delete(createUrl() + "/" + actualDto.uuid());
		// assertThat the list returned is empty
		@SuppressWarnings("rawtypes")
		final ResponseEntity<List> listOfTickets = restTemplate.getForEntity(createUrl(), List.class);
		assertTrue(listOfTickets.getStatusCode().is2xxSuccessful());
		final List<?> body = listOfTickets.getBody();
		assertNotNull(body);
		assertTrue(body.isEmpty());
	}

	@SuppressWarnings({ "all" })
	private HttpEntity createHttpEntity(final TicketDto updatedDto) {

		return new HttpEntity(updatedDto);
	}

	private String createUrl() {

		return HOSTNAME + port + RELATIVE_ENDPOINT;
	}
}
