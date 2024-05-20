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
import org.junit.jupiter.api.Assertions;
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
    public void postTicketWithNotNullFields_thenExpectFieldsToNotBeNull() {

	final UUID eventId = SqlDataStorage.EVENT_ID;
	final TicketType ticketType = TicketType.GENERAL_ADMISSION;
	final Integer price = 100;
	final Boolean transferable = false;
	final SeatingInformation seatInfo = new SeatingInformation("seat", "something");

	final TicketDto dto = new TicketDto(null, null, eventId, ticketType, price, transferable, seatInfo);

	final ResponseEntity<TicketDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, TicketDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final TicketDto actualTicketDto = responseEntity.getBody();

	assertNotNull(actualTicketDto.uuid());
	assertNotNull(actualTicketDto.lastUpdated());
	assertEquals(eventId, actualTicketDto.eventID());
	assertEquals(ticketType, actualTicketDto.ticketType());
	assertEquals(price, actualTicketDto.price());
	assertEquals(transferable, actualTicketDto.transferable());
	assertEquals(seatInfo, actualTicketDto.seatInfo());

	@SuppressWarnings("rawtypes")
	final ResponseEntity<Collection> getResponseEntity = this.restTemplate
		.getForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, Collection.class);

	assertTrue(!getResponseEntity.getBody().isEmpty());

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualTicketDto.uuid(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<TicketDto> getDeletedEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualTicketDto.uuid(), TicketDto.class);

	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @Test
    public void postTicketWithNullFieldsThenEditEvent_thenExpectFieldsToNotBeNull() {

	final UUID eventId = SqlDataStorage.EVENT_ID;
	final TicketType ticketType = TicketType.GENERAL_ADMISSION;
	final Integer price = 100;
	final Boolean transferable = false;
	final SeatingInformation seatInfo = new SeatingInformation("seat", "something");

	final TicketDto dto = new TicketDto(null, null, eventId, ticketType, price, transferable, seatInfo);

	final ResponseEntity<TicketDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, TicketDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final TicketDto entityDto = responseEntity.getBody();

	final Integer updatedPrice = 2500;
	final ResponseEntity<TicketDto> editedResponseEntity = this.restTemplate.exchange(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.uuid().toString(), HttpMethod.PUT,
		this.getHttpEntity(new TicketDto(entityDto.uuid(), null, eventId, ticketType, updatedPrice,
			!transferable, seatInfo)),
		TicketDto.class);

	final TicketDto actualTicketDto = editedResponseEntity.getBody();

	assertEquals(entityDto.uuid(), actualTicketDto.uuid());
	assertTrue(actualTicketDto.lastUpdated().after(entityDto.lastUpdated()));
	assertEquals(eventId, actualTicketDto.eventID());
	assertEquals(ticketType, actualTicketDto.ticketType());
	assertEquals(updatedPrice, actualTicketDto.price());
	assertEquals(!transferable, actualTicketDto.transferable());
	assertEquals(seatInfo, actualTicketDto.seatInfo());

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualTicketDto.uuid(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<TicketDto> getDeletedEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualTicketDto.uuid(), TicketDto.class);

	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @Test
    public void getTicketWithUUID_thenExpectToReturnTheSameExactObject() {

	final UUID eventId = SqlDataStorage.EVENT_ID;
	final TicketType ticketType = TicketType.GENERAL_ADMISSION;
	final Integer price = 100;
	final Boolean transferable = false;
	final SeatingInformation seatInfo = new SeatingInformation("seat", "something");

	final TicketDto dto = new TicketDto(null, null, eventId, ticketType, price, transferable, seatInfo);

	final ResponseEntity<TicketDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, TicketDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final TicketDto expectedEntity = responseEntity.getBody();

	final ResponseEntity<TicketDto> getEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), TicketDto.class);

	assertEquals(expectedEntity, getEntity.getBody());

    }

    @Test
    public void deleteTicketWithUUID_thenExpectForTheObjectTobeDeleted() {

	final UUID eventId = SqlDataStorage.EVENT_ID;
	final TicketType ticketType = TicketType.GENERAL_ADMISSION;
	final Integer price = 100;
	final Boolean transferable = false;
	final SeatingInformation seatInfo = new SeatingInformation("seat", "something");

	final TicketDto dto = new TicketDto(null, null, eventId, ticketType, price, transferable, seatInfo);

	final ResponseEntity<TicketDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, TicketDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final TicketDto expectedEntity = responseEntity.getBody();

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<TicketDto> getEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), TicketDto.class);

	Assertions.assertTrue(getEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<TicketDto> getDeletedEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), TicketDto.class);

	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private HttpEntity getHttpEntity(final TicketDto body) {

	return new HttpEntity(body);

    }
}
