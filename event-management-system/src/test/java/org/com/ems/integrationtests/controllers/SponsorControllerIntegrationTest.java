package org.com.ems.integrationtests.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.UUID;

import org.com.ems.EventManagementSystemApplication;
import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.dto.SponsorDto;
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
class SponsorControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost";
    private static final String RELATIVE_ENDPOINT = "/sponsor";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void postSponsorWithNonNullFields() {

	final String name = this.generateString();

	final String website = this.generateString();
	final int financialContribution = 5;
	final String email = this.generateString();
	final String addr = this.generateString();
	final ContactInformation contantInfo = new ContactInformation(email, "4323432", addr);

	final SponsorDto dto = new SponsorDto(null, null, null, name, website, financialContribution, contantInfo);

	final ResponseEntity<SponsorDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, SponsorDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final SponsorDto actualSponsorDto = responseEntity.getBody();

	assertNotNull(actualSponsorDto.id());
	assertNotNull(actualSponsorDto.lastUpdated());
	assertEquals(name, actualSponsorDto.name());
	assertEquals(contantInfo, actualSponsorDto.contactInformation());
	assertEquals(website, actualSponsorDto.website());
	assertEquals(financialContribution, actualSponsorDto.financialContribution());

	@SuppressWarnings("rawtypes")
	final ResponseEntity<Collection> getResponseEntity = this.restTemplate
		.getForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, Collection.class);

	assertTrue(!getResponseEntity.getBody().isEmpty());

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualSponsorDto.id(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<SponsorDto> getDeletedEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualSponsorDto.id(), SponsorDto.class);

	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @Test
    public void postOrganizerWithNonNullFields_then() {

	final String name = this.generateString();

	final String website = this.generateString();
	final int financialContribution = 5;
	final String email = this.generateString();
	final String addr = this.generateString();
	final ContactInformation contantInfo = new ContactInformation(email, "4323432", addr);

	final SponsorDto dto = new SponsorDto(null, null, null, name, website, financialContribution, contantInfo);

	final ResponseEntity<SponsorDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, SponsorDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final SponsorDto actualSponsorDto = responseEntity.getBody();

	assertNotNull(actualSponsorDto.id());
	assertNotNull(actualSponsorDto.createdAt());
	assertNotNull(actualSponsorDto.lastUpdated());
	assertEquals(name, actualSponsorDto.name());
	assertEquals(contantInfo, actualSponsorDto.contactInformation());
	assertEquals(website, actualSponsorDto.website());
	assertEquals(financialContribution, actualSponsorDto.financialContribution());

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualSponsorDto.id(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<SponsorDto> getDeletedEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualSponsorDto.id(), SponsorDto.class);

	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @Test
    public void postSponsorWithNonNullFields_thenEditSponsor() {

	final String name = this.generateString();

	final String website = this.generateString();
	final int financialContribution = 5;
	final String email = this.generateString();
	final String addr = this.generateString();
	final ContactInformation contantInfo = new ContactInformation(email, "4323432", addr);

	final SponsorDto dto = new SponsorDto(null, null, null, name, website, financialContribution, contantInfo);

	final ResponseEntity<SponsorDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, SponsorDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final SponsorDto entityDto = responseEntity.getBody();

	final String updatedName = this.generateString();
	final String updatedWebsite = this.generateString();
	final Integer updatedFinancialContribution = 4342342;
	final ResponseEntity<
		SponsorDto> editedResponseEntity = this.restTemplate
			.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.id().toString(),
				HttpMethod.PUT, this.getHttpEntity(new SponsorDto(entityDto.id(), null, null,
					updatedName, updatedWebsite, updatedFinancialContribution, contantInfo)),
				SponsorDto.class);

	final SponsorDto actualSponsorDto = editedResponseEntity.getBody();

	assertEquals(entityDto.id(), actualSponsorDto.id());
	assertEquals(entityDto.createdAt(), actualSponsorDto.createdAt());
	assertEquals(updatedName, actualSponsorDto.name());
	assertEquals(contantInfo, actualSponsorDto.contactInformation());
	assertEquals(updatedWebsite, actualSponsorDto.website());
	assertEquals(updatedFinancialContribution, actualSponsorDto.financialContribution());

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.id(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<SponsorDto> getDeletedEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.id(), SponsorDto.class);

	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private HttpEntity getHttpEntity(final SponsorDto body) {

	return new HttpEntity(body);

    }

    private String generateString() {

	return UUID.randomUUID().toString();

    }
}
