package org.com.ems.integrationtests.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.com.ems.EventManagementSystemApplication;
import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.dto.OrganizerDto;
import org.junit.jupiter.api.Assertions;
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

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = EventManagementSystemApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class OrganizerControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost";
    private static final String RELATIVE_ENDPOINT = "/organizer";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Order(value = 0)
    @Test
    public void postOrganizerWithNotNullFields_thenExpectFieldsToNotBeNull() {

	final String name = "name";

	final String website = "website";
	final String description = "description";
	final String email = "email";
	final String addr = "addr";
	final ContactInformation contantInfo = new ContactInformation(email, 4323432L, addr);

	final OrganizerDto dto = new OrganizerDto(null, null, name, website, description,
		List.of(EventType.WEDDING, EventType.NIGHTLIFE), contantInfo);

	final ResponseEntity<OrganizerDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, OrganizerDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final OrganizerDto actualEntity = responseEntity.getBody();

	assertNotNull(actualEntity.uuid());
	assertNotNull(actualEntity.lastUpdated());
	assertEquals(name, actualEntity.name());
	assertEquals(contantInfo, actualEntity.contactInformation());
	assertEquals(website, actualEntity.website());
	assertEquals(description, actualEntity.description());

    }

    @Order(value = 1)
    @Test
    public void postOrganizerWithNullFieldsThenEditEvent_thenExpectFieldsToNotBeNull() {

	final String name = "name2";
	final String website = "website2";
	final String description = "description";
	final String email = "email2";
	final String addr = "addr2";
	final ContactInformation contantInfo = new ContactInformation(email, 4323432L, addr);

	final List<EventType> eventTypes = List.of(EventType.WEDDING, EventType.NIGHTLIFE);

	final OrganizerDto dto = new OrganizerDto(null, null, name, website, description, eventTypes, contantInfo);

	final ResponseEntity<OrganizerDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, OrganizerDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final OrganizerDto entityDto = responseEntity.getBody();

	final ResponseEntity<
		OrganizerDto> editedResponseEntity = this.restTemplate
			.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.uuid().toString(),
				HttpMethod.PUT, this.getHttpEntity(new OrganizerDto(entityDto.uuid(), null,
					name + description, website, description + name, eventTypes, contantInfo)),
				OrganizerDto.class);

	final OrganizerDto actualEntity = editedResponseEntity.getBody();

	assertEquals(entityDto.uuid(), actualEntity.uuid());
	assertTrue(actualEntity.lastUpdated().after(entityDto.lastUpdated()));
	assertEquals(name + description, actualEntity.name());
	assertEquals(contantInfo, actualEntity.contactInformation());
	assertEquals(website, actualEntity.website());
	assertEquals(eventTypes, actualEntity.eventTypes());
	assertEquals(description + name, actualEntity.description());

    }

    @Order(value = 2)
    @Test
    public void getOrganizers_thenExpectToHave3InTheDb() {

	@SuppressWarnings("rawtypes")
	final ResponseEntity<Collection> responseEntity = this.restTemplate
		.getForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, Collection.class);

	assertEquals(2, responseEntity.getBody().size());

    }

    @Order(value = 3)
    @Test
    public void getOrganizerWithUUID_thenExpectToReturnTheSameExactObject() {

	final String name = "name3";

	final String website = "website3";
	final String description = "description3";
	final String email = "email3";
	final String addr = "addr3";
	final ContactInformation contantInfo = new ContactInformation(email, 4323432L, addr);

	final List<EventType> eventTypes = List.of(EventType.WEDDING, EventType.NIGHTLIFE);

	final OrganizerDto dto = new OrganizerDto(null, null, name, website, description, eventTypes, contantInfo);

	final ResponseEntity<OrganizerDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, OrganizerDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final OrganizerDto expectedEntity = responseEntity.getBody();

	final ResponseEntity<OrganizerDto> getEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), OrganizerDto.class);

	assertEquals(expectedEntity, getEntity.getBody());

    }

    @Order(value = 4)
    @Test
    public void deleteOrganizerWithUUID_thenExpectForTheObjectTobeDeleted() {

	final String name = "name5";

	final String website = "website5";
	final String description = "description5";
	final String email = "email5";
	final String addr = "addr5";
	final ContactInformation contantInfo = new ContactInformation(email, 4323432L, addr);

	final List<EventType> eventTypes = List.of(EventType.WEDDING, EventType.NIGHTLIFE);

	final OrganizerDto dto = new OrganizerDto(null, null, name, website, description, eventTypes, contantInfo);

	final ResponseEntity<OrganizerDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, OrganizerDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final OrganizerDto expectedEntity = responseEntity.getBody();

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<OrganizerDto> getEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), OrganizerDto.class);

	Assertions.assertTrue(getEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private HttpEntity getHttpEntity(final OrganizerDto body) {

	return new HttpEntity(body);

    }
}
