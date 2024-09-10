package io.github.evaggelos99.ems.sponsor.service.controller;

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

import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.sponsor.api.SponsorDto;
import io.github.evaggelos99.ems.sponsor.api.util.SponsorObjectGenerator;
import io.github.evaggelos99.ems.sponsor.service.SponsorServiceApplication;
import io.github.evaggelos99.ems.sponsor.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.sponsor.service.util.TestConfiguration;

@SpringBootTest(classes = { SponsorServiceApplication.class,
		TestConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class SponsorControllerIntegrationTest {

	private static final String HOSTNAME = "http://localhost:";
	private static final String RELATIVE_ENDPOINT = "/sponsor";
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
	void postSponsor_getSponsor_deleteSponsor_getSponsor_whenInvokedWithValidSponsorDto_thenExpectForSponsorToBeAddedFetchedAndDeleted() {

		final Instant currentTime = Instant.now();
		final SponsorDto dto = SponsorObjectGenerator.generateSponsorDtoWithoutTimestamps();

		final ResponseEntity<SponsorDto> actualEntity = restTemplate.postForEntity(createUrl(), dto, SponsorDto.class);

		assertTrue(actualEntity.getStatusCode().is2xxSuccessful());

		final SponsorDto actualDto = actualEntity.getBody();
		// assert
		assertNotNull(actualDto);
		assertEquals(dto.uuid(), actualDto.uuid());
		assertTrue(actualDto.createdAt().isAfter(currentTime));
		assertTrue(actualDto.lastUpdated().isAfter(currentTime));
		assertEquals(dto.name(), actualDto.name());
		assertEquals(dto.website(), actualDto.website());
		assertEquals(dto.financialContribution(), actualDto.financialContribution());
		assertEquals(dto.contactInformation(), actualDto.contactInformation());

		final ResponseEntity<SponsorDto> actualGetEntity = restTemplate
				.getForEntity(createUrl() + "/" + actualDto.uuid(), SponsorDto.class);
		// assert
		assertTrue(actualGetEntity.getStatusCode().is2xxSuccessful());

		final SponsorDto getDto = actualGetEntity.getBody();
		assertNotNull(getDto);
		assertEquals(actualDto.uuid(), getDto.uuid());
		assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
				getDto.createdAt().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
		assertEquals(actualDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS),
				getDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
		assertEquals(actualDto.name(), getDto.name());
		assertEquals(actualDto.website(), getDto.website());
		assertEquals(actualDto.financialContribution(), getDto.financialContribution());
		assertEquals(actualDto.contactInformation(), getDto.contactInformation());

		// deleteOrganizer
		final ResponseEntity<Void> deletedEntity = restTemplate.exchange(createUrl() + "/" + actualDto.uuid(),
				HttpMethod.DELETE, null, Void.class);
		assertTrue(deletedEntity.getStatusCode().is2xxSuccessful());
		// assertThat it cannot be found
		final ResponseEntity<SponsorDto> deletedDto = restTemplate.getForEntity(createUrl() + "/" + actualDto.uuid(),
				SponsorDto.class);
		assertTrue(deletedDto.getStatusCode().is2xxSuccessful());
		assertNull(deletedDto.getBody());
	}

	@Test
	void postOrganizer_putOrganizer_getOrganizer_deleteOrganizer_getAll_whenInvokedWithValidSponsorDto_thenExpectForOrganizerToBeAddedThenEditedThenDeleted() {

		final Instant currentTime = Instant.now();
		final SponsorDto dto = SponsorObjectGenerator.generateSponsorDtoWithoutTimestamps();
		// postOrganizer
		final ResponseEntity<SponsorDto> actualEntity = restTemplate.postForEntity(createUrl(), dto, SponsorDto.class);
		assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
		final SponsorDto actualDto = actualEntity.getBody();
		// assert
		assertNotNull(actualDto);
		assertEquals(dto.uuid(), actualDto.uuid());
		assertTrue(actualDto.createdAt().isAfter(currentTime));
		assertTrue(actualDto.lastUpdated().isAfter(currentTime));
		assertEquals(dto.name(), actualDto.name());
		assertEquals(dto.website(), actualDto.website());
		assertEquals(dto.financialContribution(), actualDto.financialContribution());
		assertEquals(dto.contactInformation(), actualDto.contactInformation());
		// putOrganizer
		final ContactInformation contactInformation = SponsorObjectGenerator.generateContactInformation();

		final SponsorDto updatedDto = new SponsorDto(actualDto.uuid(), null, null, UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), 1500, contactInformation);

		final ResponseEntity<SponsorDto> actualPutEntity = restTemplate.exchange(createUrl() + "/" + actualDto.uuid(),
				HttpMethod.PUT, createHttpEntity(updatedDto), SponsorDto.class);
		final SponsorDto actualPutDto = actualPutEntity.getBody();
		// assert
		assertTrue(actualPutEntity.getStatusCode().is2xxSuccessful());
		assertNotNull(actualPutDto);
		assertEquals(updatedDto.uuid(), actualPutDto.uuid());
		assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
				actualPutDto.createdAt().truncatedTo(ChronoUnit.MILLIS));
		assertTrue(actualPutDto.lastUpdated().isAfter(actualDto.lastUpdated()));
		assertEquals(updatedDto.name(), actualPutDto.name());
		assertEquals(updatedDto.website(), actualPutDto.website());
		assertEquals(updatedDto.financialContribution(), actualPutDto.financialContribution());
		assertEquals(updatedDto.contactInformation(), actualPutDto.contactInformation());

		// deleteOrganizer
		restTemplate.delete(createUrl() + "/" + actualDto.uuid());
		// assertThat the list returned is empty
		@SuppressWarnings("rawtypes")
		final ResponseEntity<List> listOfOrganizers = restTemplate.getForEntity(createUrl(), List.class);
		assertTrue(listOfOrganizers.getStatusCode().is2xxSuccessful());
		final List<?> body = listOfOrganizers.getBody();
		assertNotNull(body);
		assertTrue(body.isEmpty());
	}

	@SuppressWarnings({ "all" })
	private HttpEntity createHttpEntity(final SponsorDto updatedDto) {

		return new HttpEntity(updatedDto);
	}

	private String createUrl() {

		return HOSTNAME + this.port + RELATIVE_ENDPOINT;
	}

}
