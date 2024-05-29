package org.com.ems.integrationtests.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.UUID;

import org.com.ems.EventManagementSystemApplication;
import org.com.ems.api.domainobjects.SeatingInformation;
import org.com.ems.api.domainobjects.TicketType;
import org.com.ems.api.dto.TicketDto;
import org.com.ems.util.SqlDataStorage;
import org.com.ems.util.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = { EventManagementSystemApplication.class,
	TestConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-tests")
class TicketControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost";
    private static final String RELATIVE_ENDPOINT = "/ticket";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void postTicketWithNonNullFields() {

	final UUID eventId = SqlDataStorage.EVENT_ID;
	final TicketType ticketType = TicketType.GENERAL_ADMISSION;
	final Integer price = 100;
	final Boolean transferable = false;
	final SeatingInformation seatInfo = new SeatingInformation("seat", "something");

	final TicketDto dto = new TicketDto(null, null, null, eventId, ticketType, price, transferable, seatInfo);

	final ResponseEntity<TicketDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, TicketDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final TicketDto actualTicketDto = responseEntity.getBody();

	assertNotNull(actualTicketDto.id());
	assertNotNull(actualTicketDto.createdAt());
	assertNotNull(actualTicketDto.lastUpdated());
	assertEquals(eventId, actualTicketDto.eventID());
	assertEquals(ticketType, actualTicketDto.ticketType());
	assertEquals(price, actualTicketDto.price());
	assertEquals(transferable, actualTicketDto.transferable());
	assertEquals(seatInfo, actualTicketDto.seatInformation());

	@SuppressWarnings("rawtypes")
	final ResponseEntity<Collection> getResponseEntity = this.restTemplate
		.getForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, Collection.class);

	assertTrue(!getResponseEntity.getBody().isEmpty());

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualTicketDto.id(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<TicketDto> getDeletedEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualTicketDto.id(), TicketDto.class);

	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @Test
    void postTicketWithNonNullFields_thenPutTicket() {

	final UUID eventId = SqlDataStorage.EVENT_ID;
	final TicketType ticketType = TicketType.GENERAL_ADMISSION;
	final Integer price = 100;
	final Boolean transferable = false;
	final SeatingInformation seatInfo = new SeatingInformation("seat", "something");

	final TicketDto dto = new TicketDto(null, null, null, eventId, ticketType, price, transferable, seatInfo);

	final ResponseEntity<TicketDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, TicketDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final TicketDto entityDto = responseEntity.getBody();

	final Integer updatedPrice = 2500;
	final ResponseEntity<TicketDto> editedResponseEntity = this.restTemplate.exchange(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.id().toString(), HttpMethod.PUT,
		this.getHttpEntity(new TicketDto(entityDto.id(), null, null, eventId, ticketType, updatedPrice,
			!transferable, seatInfo)),
		TicketDto.class);

	final TicketDto actualTicketDto = editedResponseEntity.getBody();

	assertEquals(entityDto.id(), actualTicketDto.id());
	assertEquals(entityDto.createdAt(), actualTicketDto.createdAt());
	assertEquals(eventId, actualTicketDto.eventID());
	assertEquals(ticketType, actualTicketDto.ticketType());
	assertEquals(updatedPrice, actualTicketDto.price());
	assertEquals(!transferable, actualTicketDto.transferable());
	assertEquals(seatInfo, actualTicketDto.seatInformation());

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualTicketDto.id(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<TicketDto> getDeletedEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualTicketDto.id(), TicketDto.class);

	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private HttpEntity getHttpEntity(final TicketDto body) {

	return new HttpEntity(body);

    }
}
