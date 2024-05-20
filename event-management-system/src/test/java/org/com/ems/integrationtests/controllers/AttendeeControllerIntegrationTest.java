package org.com.ems.integrationtests.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.com.ems.EventManagementSystemApplication;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.util.DbConfiguration;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = { EventManagementSystemApplication.class,
	DbConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-tests")
public class AttendeeControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost";
    private static final String RELATIVE_ENDPOINT = "/attendee";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Order(value = 0)
    @Test
    public void postAttendeeWithNotNullFields_thenExpectFieldsToNotBeNull() {

	final String firstName = "first";
	final String lastName = "last";

	final AttendeeDto dto = new AttendeeDto(null, null, firstName, lastName, List.of());

	final ResponseEntity<AttendeeDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, AttendeeDto.class);

	System.out.println(responseEntity);
	assertEquals(201, responseEntity.getStatusCode().value());

	final AttendeeDto actualEntity = responseEntity.getBody();

	assertNotNull(actualEntity.uuid());
	assertNotNull(actualEntity.lastUpdated());
	assertEquals(firstName, actualEntity.firstName());
	assertEquals(lastName, actualEntity.lastName());
	assertNull(actualEntity.ticketIDs());

    }

    @Order(value = 1)
    @Test
    public void postAttendeeWithNullFields_thenExpectFieldsToNotBeNull() {

	final String firstName = "first";
	final String lastName = "last";
	final UUID expectedUUID = UUID.randomUUID();

	final AttendeeDto dto = new AttendeeDto(null, null, firstName, lastName, List.of(expectedUUID));

	final ResponseEntity<AttendeeDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, AttendeeDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final AttendeeDto actualEntity = responseEntity.getBody();

	assertNotNull(actualEntity.uuid());
	assertNotNull(actualEntity.lastUpdated());
	assertEquals(firstName, actualEntity.firstName());
	assertEquals(lastName, actualEntity.lastName());
	assertEquals(List.of(expectedUUID), actualEntity.ticketIDs());

    }

    @Order(value = 2)
    @Test
    public void postAttendeeWithNullFieldsThenEditAttendee_thenExpectFieldsToNotBeNull() {

	final String firstName = "first";
	final String lastName = "last";

	final AttendeeDto dto = new AttendeeDto(null, null, firstName, lastName, null);

	final ResponseEntity<AttendeeDto> ogResponseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, AttendeeDto.class);

	assertEquals(201, ogResponseEntity.getStatusCode().value());

	final AttendeeDto entityDto = ogResponseEntity.getBody();

	final ResponseEntity<AttendeeDto> editedResponseEntity = this.restTemplate.exchange(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.uuid().toString(), HttpMethod.PUT,
		this.getHttpEntity(new AttendeeDto(entityDto.uuid(), null, firstName + lastName, lastName, null)),
		AttendeeDto.class);

	final AttendeeDto actualEntity = editedResponseEntity.getBody();

	assertEquals(entityDto.uuid(), actualEntity.uuid());
	assertTrue(actualEntity.lastUpdated().after(entityDto.lastUpdated()));
	assertEquals(firstName + lastName, actualEntity.firstName());
	assertEquals(lastName, actualEntity.lastName());
	assertNull(actualEntity.ticketIDs());

    }

    @Order(value = 3)
    @Test
    public void getAttendees_thenExpectToHave3InTheDb() {

	@SuppressWarnings("rawtypes")
	final ResponseEntity<Collection> responseEntity = this.restTemplate
		.getForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, Collection.class);

	System.out.println(responseEntity);

	assertEquals(3, responseEntity.getBody().size());

    }

    @Order(value = 4)
    @Test
    public void getAttendeeWithUUID_thenExpectToReturnTheSameExactObject() {

	final String firstName = "first";
	final String lastName = "last";
	final UUID expectedUUID = UUID.randomUUID();

	final AttendeeDto dto = new AttendeeDto(null, null, firstName, lastName, List.of(expectedUUID));

	final ResponseEntity<AttendeeDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, AttendeeDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final AttendeeDto expectedEntity = responseEntity.getBody();

	final ResponseEntity<AttendeeDto> getEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), AttendeeDto.class);

	assertEquals(expectedEntity, getEntity.getBody());

    }

    @Order(value = 5)
    @Test
    public void deleteAttendeeWithUUID_thenExpectForTheObjectTobeDeleted() {

	final String firstName = "first";
	final String lastName = "last";
	final UUID expectedUUID = UUID.randomUUID();

	final AttendeeDto dto = new AttendeeDto(null, null, firstName, lastName, List.of(expectedUUID));

	final ResponseEntity<AttendeeDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, AttendeeDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final AttendeeDto expectedEntity = responseEntity.getBody();

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<AttendeeDto> getEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), AttendeeDto.class);

	assertTrue(getEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private HttpEntity getHttpEntity(final AttendeeDto body) {

	return new HttpEntity(body);

    }

}
