package org.com.ems.integrationtests.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.com.ems.EventManagementSystemApplication;
import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.dto.OrganizerDto;
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
class OrganizerControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost";
    private static final String RELATIVE_ENDPOINT = "/organizer";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void postOrganizerWithNonNullFields() {

	final String name = this.generateString();

	final String website = this.generateString();
	final String description = this.generateString();
	final String email = this.generateString();
	final String addr = this.generateString();
	final ContactInformation contantInfo = new ContactInformation(email, "4323432", addr);

	final OrganizerDto dto = new OrganizerDto(null, null, null, name, website, description,
		List.of(EventType.WEDDING, EventType.NIGHTLIFE), contantInfo);

	final ResponseEntity<OrganizerDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, OrganizerDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final OrganizerDto actualOrganizerDto = responseEntity.getBody();

	assertNotNull(actualOrganizerDto.id());
	assertNotNull(actualOrganizerDto.createdAt());
	assertNotNull(actualOrganizerDto.lastUpdated());
	assertEquals(name, actualOrganizerDto.name());
	assertEquals(contantInfo, actualOrganizerDto.contactInformation());
	assertEquals(website, actualOrganizerDto.website());
	assertEquals(description, actualOrganizerDto.information());

	@SuppressWarnings("rawtypes")
	final ResponseEntity<Collection> getResponseEntity = this.restTemplate
		.getForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, Collection.class);

	assertTrue(!getResponseEntity.getBody().isEmpty());

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualOrganizerDto.id(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<OrganizerDto> getDeletedEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualOrganizerDto.id(), OrganizerDto.class);

	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @Test
    public void postOrganizerWithNonNullFields_thenPutOrganizerWithUpdatedFields() {

	final String name = "name2";
	final String website = "website2";
	final String description = "description";
	final String email = "email2";
	final String addr = "addr2";
	final ContactInformation contantInfo = new ContactInformation(email, "4323432", addr);

	final List<EventType> eventTypes = List.of(EventType.WEDDING, EventType.NIGHTLIFE);

	final OrganizerDto dto = new OrganizerDto(null, null, null, name, website, description, eventTypes,
		contantInfo);

	final ResponseEntity<OrganizerDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, OrganizerDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final OrganizerDto entityDto = responseEntity.getBody();

	final String updatedName = this.generateString();
	final String updatedWebsite = this.generateString();
	final String updatedDescription = this.generateString();

	final ResponseEntity<OrganizerDto> editedResponseEntity = //
		this.restTemplate
			.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.id().toString(),
				HttpMethod.PUT,
				this.getHttpEntity(new OrganizerDto(entityDto.id(), null, null, updatedName,
					updatedWebsite, updatedDescription, eventTypes, contantInfo)),
				OrganizerDto.class);

	final OrganizerDto actualOrganizerDto = editedResponseEntity.getBody();

	assertEquals(entityDto.id(), actualOrganizerDto.id());
	assertEquals(entityDto.createdAt(), actualOrganizerDto.createdAt());
	assertEquals(updatedName, actualOrganizerDto.name());
	assertEquals(contantInfo, actualOrganizerDto.contactInformation());
	assertEquals(updatedWebsite, actualOrganizerDto.website());
	assertEquals(eventTypes, actualOrganizerDto.eventTypes());
	assertEquals(updatedDescription, actualOrganizerDto.information());

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualOrganizerDto.id(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<OrganizerDto> getDeletedEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualOrganizerDto.id(), OrganizerDto.class);

	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @Test
    public void getOrganizerWithId() {

	final String name = this.generateString();

	final String website = this.generateString();
	final String description = this.generateString();
	final String email = this.generateString();
	final String addr = this.generateString();
	final ContactInformation contantInfo = new ContactInformation(email, "4323432", addr);

	final List<EventType> eventTypes = List.of(EventType.WEDDING, EventType.NIGHTLIFE);

	final OrganizerDto dto = new OrganizerDto(null, null, null, name, website, description, eventTypes,
		contantInfo);

	final ResponseEntity<OrganizerDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, OrganizerDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final OrganizerDto expectedEntity = responseEntity.getBody();

	final ResponseEntity<OrganizerDto> getEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.id(), OrganizerDto.class);

	assertEquals(expectedEntity, getEntity.getBody());

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.id(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<OrganizerDto> getDeletedEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.id(), OrganizerDto.class);

	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private HttpEntity getHttpEntity(final OrganizerDto body) {

	return new HttpEntity(body);

    }

    private String generateString() {

	return UUID.randomUUID().toString();

    }

}
