package org.com.ems.integrationtests.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.com.ems.EventManagementSystemApplication;
import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.dto.SponsorDto;
import org.com.ems.util.TestConfiguration;
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
import org.springframework.test.context.ActiveProfiles;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = { EventManagementSystemApplication.class,
	TestConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-tests")
class SponsorControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost";
    private static final String RELATIVE_ENDPOINT = "/sponsor";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Order(value = 0)
    @Test
    public void postSponsorWithNotNullFields_thenExpectFieldsToNotBeNull() {

	final String name = "name";

	final String website = "website";
	final int financialContribution = 5;
	final String email = "email";
	final String addr = "addr";
	final ContactInformation contantInfo = new ContactInformation(email, "4323432", addr);

	final SponsorDto dto = new SponsorDto(null, null, name, website, financialContribution, contantInfo);

	final ResponseEntity<SponsorDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, SponsorDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final SponsorDto actualEntity = responseEntity.getBody();

	assertNotNull(actualEntity.uuid());
	assertNotNull(actualEntity.lastUpdated());
	assertEquals(name, actualEntity.denomination());
	assertEquals(contantInfo, actualEntity.contactInformation());
	assertEquals(website, actualEntity.website());
	assertEquals(financialContribution, actualEntity.financialContribution());

    }

    @Order(value = 2)
    @Test
    public void postEventWithNullFieldsThenEditEvent_thenExpectFieldsToNotBeNull() {

	final String name = "name";

	final String website = "website";
	final int financialContribution = 5;
	final String email = "email";
	final String addr = "addr";
	final ContactInformation contantInfo = new ContactInformation(email, "4323432", addr);

	final SponsorDto dto = new SponsorDto(null, null, name, website, financialContribution, contantInfo);

	final ResponseEntity<SponsorDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, SponsorDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final SponsorDto entityDto = responseEntity.getBody();

	final ResponseEntity<SponsorDto> editedResponseEntity = this.restTemplate.exchange(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.uuid().toString(), HttpMethod.PUT,
		this.getHttpEntity(
			new SponsorDto(entityDto.uuid(), null, name, website, financialContribution, contantInfo)),
		SponsorDto.class);

	final SponsorDto actualEntity = editedResponseEntity.getBody();

	assertEquals(entityDto.uuid(), actualEntity.uuid());
	assertTrue(actualEntity.lastUpdated().after(entityDto.lastUpdated()));
	assertEquals(name, actualEntity.denomination());
	assertEquals(contantInfo, actualEntity.contactInformation());
	assertEquals(website, actualEntity.website());
	assertEquals(financialContribution, actualEntity.financialContribution());

    }

    @Order(value = 3)
    @Test
    public void getEvents_thenExpectToHave3InTheDb() {

	@SuppressWarnings("rawtypes")
	final ResponseEntity<Collection> responseEntity = this.restTemplate
		.getForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, Collection.class);

	assertEquals(2, responseEntity.getBody().size());

    }

    @Order(value = 4)
    @Test
    public void getEventWithUUID_thenExpectToReturnTheSameExactObject() {

	final String name = "name";

	final String website = "website";
	final int financialContribution = 5;
	final String email = "email";
	final String addr = "addr";
	final ContactInformation contantInfo = new ContactInformation(email, "4323432", addr);

	final SponsorDto dto = new SponsorDto(null, null, name, website, financialContribution, contantInfo);

	final ResponseEntity<SponsorDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, SponsorDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final SponsorDto expectedEntity = responseEntity.getBody();

	final ResponseEntity<SponsorDto> getEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), SponsorDto.class);

	assertEquals(expectedEntity, getEntity.getBody());

    }

    @Order(value = 5)
    @Test
    public void deleteAttendeeWithUUID_thenExpectForTheObjectTobeDeleted() {

	final String name = "name";

	final String website = "website";
	final int financialContribution = 5;
	final String email = "email";
	final String addr = "addr";
	final ContactInformation contantInfo = new ContactInformation(email, "4323432", addr);

	final SponsorDto dto = new SponsorDto(null, null, name, website, financialContribution, contantInfo);

	final ResponseEntity<SponsorDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, SponsorDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final SponsorDto expectedEntity = responseEntity.getBody();

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<SponsorDto> getEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), SponsorDto.class);

	Assertions.assertTrue(getEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private HttpEntity getHttpEntity(final SponsorDto body) {

	return new HttpEntity(body);

    }
}
