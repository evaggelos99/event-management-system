//package com.github.evaggelos99.ems.integrationtests.controllers;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.UUID;
//
//import org.com.ems.EventManagementSystemApplication;
//import org.com.ems.api.dto.AttendeeDto;
//import org.com.ems.util.SqlDataStorage;
//import org.com.ems.util.TestConfiguration;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest(classes = { EventManagementSystemApplication.class,
//	TestConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("integration-tests")
//public class AttendeeControllerIntegrationTest {
//
//    private static final String HOSTNAME = "http://localhost";
//    private static final String RELATIVE_ENDPOINT = "/attendee";
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    public void postAttendeeWithTicketIdsNull() {
//
//	final String firstName = this.generateString();
//	final String lastName = this.generateString();
//
//	final AttendeeDto dto = new AttendeeDto(null, null, null, firstName, lastName, null);
//
//	final ResponseEntity<AttendeeDto> responseEntity = this.restTemplate
//		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, AttendeeDto.class);
//
//	assertEquals(201, responseEntity.getStatusCode().value());
//
//	final AttendeeDto actualAttendeeDto = responseEntity.getBody();
//
//	assertNotNull(actualAttendeeDto.uuid());
//	assertNotNull(actualAttendeeDto.createdAt());
//	assertNotNull(actualAttendeeDto.lastUpdated());
//	assertEquals(firstName, actualAttendeeDto.firstName());
//	assertEquals(lastName, actualAttendeeDto.lastName());
//	assertTrue(actualAttendeeDto.ticketIDs().isEmpty());
//
//	@SuppressWarnings("rawtypes")
//	final ResponseEntity<Collection> getResponseEntity = this.restTemplate
//		.getForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, Collection.class);
//
//	assertTrue(!getResponseEntity.getBody().isEmpty());
//
//	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualAttendeeDto.uuid(),
//		HttpMethod.DELETE, null, Void.class);
//
//	final ResponseEntity<AttendeeDto> getDeletedEntity = this.restTemplate.getForEntity(
//		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualAttendeeDto.uuid(), AttendeeDto.class);
//
//	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));
//
//    }
//
//    @Test
//    public void postAttendeeWithTicketIds() {
//
//	final String firstName = this.generateString();
//	final String lastName = this.generateString();
//	final UUID expectedUUID = SqlDataStorage.TICKET_ID;
//
//	final AttendeeDto dto = new AttendeeDto(null, null, null, firstName, lastName, List.of(expectedUUID));
//
//	final ResponseEntity<AttendeeDto> responseEntity = this.restTemplate
//		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, AttendeeDto.class);
//
//	assertEquals(201, responseEntity.getStatusCode().value());
//
//	final AttendeeDto actualAttendeeDto = responseEntity.getBody();
//
//	assertNotNull(actualAttendeeDto.uuid());
//	assertNotNull(actualAttendeeDto.createdAt());
//	assertNotNull(actualAttendeeDto.lastUpdated());
//	assertEquals(firstName, actualAttendeeDto.firstName());
//	assertEquals(lastName, actualAttendeeDto.lastName());
//	assertEquals(List.of(expectedUUID), actualAttendeeDto.ticketIDs());
//
//	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualAttendeeDto.uuid(),
//		HttpMethod.DELETE, null, Void.class);
//
//	final ResponseEntity<AttendeeDto> getDeletedEntity = this.restTemplate.getForEntity(
//		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualAttendeeDto.uuid(), AttendeeDto.class);
//
//	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));
//
//    }
//
//    @Test
//    public void postAttendeeWithNullTicketIds_putAttendeeWithNullTicketIds() {
//
//	final String firstName = this.generateString();
//	final String lastName = this.generateString();
//
//	final AttendeeDto dto = new AttendeeDto(null, null, null, firstName, lastName, null);
//
//	final ResponseEntity<AttendeeDto> ogResponseEntity = this.restTemplate
//		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, AttendeeDto.class);
//
//	assertEquals(201, ogResponseEntity.getStatusCode().value());
//
//	final AttendeeDto entityDto = ogResponseEntity.getBody();
//
//	final String updatedName = this.generateString();
//	final String updatedLastName = this.generateString();
//	final ResponseEntity<AttendeeDto> editedResponseEntity = this.restTemplate.exchange(
//		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.uuid().toString(), HttpMethod.PUT,
//		this.getHttpEntity(new AttendeeDto(entityDto.uuid(), null, null, updatedName, updatedLastName, null)),
//		AttendeeDto.class);
//
//	final AttendeeDto actualAttendeeDto = editedResponseEntity.getBody();
//
//	assertEquals(entityDto.uuid(), actualAttendeeDto.uuid());
//	assertEquals(actualAttendeeDto.createdAt(), entityDto.createdAt());
//	assertEquals(updatedName, actualAttendeeDto.firstName());
//	assertEquals(updatedLastName, actualAttendeeDto.lastName());
//	assertNotNull(actualAttendeeDto.ticketIDs());
//
//	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.uuid(),
//		HttpMethod.DELETE, null, Void.class);
//
//	final ResponseEntity<AttendeeDto> getDeletedEntity = this.restTemplate.getForEntity(
//		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.uuid(), AttendeeDto.class);
//
//	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));
//
//    }
//
//    @Test
//    public void getAttendeeWithId() {
//
//	final String firstName = this.generateString();
//	final String lastName = this.generateString();
//	final UUID expectedUUID = UUID.randomUUID();
//
//	final AttendeeDto dto = new AttendeeDto(null, null, null, firstName, lastName, List.of(expectedUUID));
//
//	final ResponseEntity<AttendeeDto> responseEntity = this.restTemplate
//		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, AttendeeDto.class);
//
//	assertEquals(201, responseEntity.getStatusCode().value());
//
//	final AttendeeDto expectedEntity = responseEntity.getBody();
//
//	final ResponseEntity<AttendeeDto> getEntity = this.restTemplate.getForEntity(
//		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), AttendeeDto.class);
//
//	assertEquals(expectedEntity, getEntity.getBody());
//
//	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(),
//		HttpMethod.DELETE, null, Void.class);
//
//	final ResponseEntity<AttendeeDto> getDeletedEntity = this.restTemplate.getForEntity(
//		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), AttendeeDto.class);
//
//	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));
//
//    }
//
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    private HttpEntity getHttpEntity(final AttendeeDto body) {
//
//	return new HttpEntity(body);
//
//    }
//
//    private String generateString() {
//
//	return UUID.randomUUID().toString();
//
//    }
//
//}
