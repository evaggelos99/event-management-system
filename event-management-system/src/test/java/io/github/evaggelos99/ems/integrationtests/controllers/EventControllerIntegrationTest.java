//package io.github.evaggelos99.ems.integrationtests.controllers;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Collection;
//import java.util.List;
//import java.util.UUID;
//
//import io.github.evaggelos99.ems.EventManagementSystemApplication;
//import io.github.evaggelos99.ems.api.domainobjects.EventType;
//import io.github.evaggelos99.ems.api.dto.EventDto;
//import io.github.evaggelos99.ems.util.SqlDataStorage;
//import io.github.evaggelos99.ems.util.TestConfiguration;
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
//class EventControllerIntegrationTest {
//
//    private static final String HOSTNAME = "http://localhost";
//    private static final String RELATIVE_ENDPOINT = "/event";
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    public void postEventWithoutNullFields() {
//
//	final String name = this.generateString();
//	final String place = this.generateString();
//
//	final EventType eventType = EventType.CONFERENCE;
//	final UUID attendeeId = SqlDataStorage.ATTENDEE_ID;
//	final UUID organizerId = SqlDataStorage.ORGANIZER_ID;
//	final UUID sponsorId = SqlDataStorage.SPONSOR_ID;
//	final LocalDateTime localDateTime = LocalDateTime.now();
//	final Duration duration = Duration.ZERO;
//	final int limitOfPeople = 1000;
//
//	final EventDto dto = new EventDto(null, null, null, name, place, eventType, List.of(attendeeId), organizerId,
//		limitOfPeople, List.of(sponsorId), localDateTime, duration);
//
//	final ResponseEntity<EventDto> responseEntity = this.restTemplate
//		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, EventDto.class);
//
//	assertEquals(201, responseEntity.getStatusCode().value());
//
//	final EventDto actualEventDto = responseEntity.getBody();
//
//	assertNotNull(actualEventDto.uuid());
//	assertNotNull(actualEventDto.createdAt());
//	assertNotNull(actualEventDto.lastUpdated());
//	assertEquals(name, actualEventDto.denomination());
//	assertEquals(place, actualEventDto.place());
//	assertEquals(eventType, actualEventDto.eventType());
//
//	assertEquals(List.of(attendeeId), actualEventDto.attendeesIds());
//	assertEquals(organizerId, actualEventDto.organizerId());
//	assertEquals(limitOfPeople, actualEventDto.limitOfPeople());
//	assertEquals(List.of(sponsorId), actualEventDto.sponsorsIds());
//	assertEquals(localDateTime, actualEventDto.startTimeOfEvent());
//	assertEquals(duration, actualEventDto.duration());
//
//	@SuppressWarnings("rawtypes")
//	final ResponseEntity<Collection> getResponseEntity = this.restTemplate
//		.getForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, Collection.class);
//
//	assertTrue(!getResponseEntity.getBody().isEmpty());
//
//	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualEventDto.uuid(),
//		HttpMethod.DELETE, null, Void.class);
//
//	final ResponseEntity<EventDto> getDeletedEntity = this.restTemplate.getForEntity(
//		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualEventDto.uuid(), EventDto.class);
//
//	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));
//
//    }
//
//    @Test
//    public void postEventWithNullFields() {
//
//	final String name = this.generateString();
//	final String place = this.generateString();
//
//	final EventType eventType = EventType.CONFERENCE;
//	final UUID attendeeId = SqlDataStorage.ATTENDEE_ID;
//	final UUID organizerId = SqlDataStorage.ORGANIZER_ID;
//	final LocalDateTime localDateTime = LocalDateTime.now();
//	final Duration duration = Duration.ZERO;
//	final int limitOfPeople = 1000;
//
//	final EventDto dto = new EventDto(null, null, null, name, place, eventType, List.of(attendeeId), organizerId,
//		limitOfPeople, null, localDateTime, duration);
//
//	final ResponseEntity<EventDto> responseEntity = this.restTemplate
//		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, EventDto.class);
//
//	assertEquals(201, responseEntity.getStatusCode().value());
//
//	final EventDto actualEventDto = responseEntity.getBody();
//
//	assertNotNull(actualEventDto.uuid());
//	assertNotNull(actualEventDto.createdAt());
//	assertNotNull(actualEventDto.lastUpdated());
//	assertEquals(name, actualEventDto.denomination());
//	assertEquals(place, actualEventDto.place());
//	assertEquals(eventType, actualEventDto.eventType());
//
//	assertEquals(List.of(attendeeId), actualEventDto.attendeesIds());
//	assertEquals(organizerId, actualEventDto.organizerId());
//	assertEquals(limitOfPeople, actualEventDto.limitOfPeople());
//	assertNotNull(actualEventDto.sponsorsIds());
//	assertEquals(localDateTime, actualEventDto.startTimeOfEvent());
//	assertEquals(duration, actualEventDto.duration());
//
//	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualEventDto.uuid(),
//		HttpMethod.DELETE, null, Void.class);
//
//	final ResponseEntity<EventDto> getDeletedEntity = this.restTemplate.getForEntity(
//		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualEventDto.uuid(), EventDto.class);
//
//	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));
//
//    }
//
//    @Test
//    public void postEventWithoutNullFields_thenPutEventWithUpdatedFields() {
//
//	final String name = this.generateString();
//	final String place = this.generateString();
//
//	final EventType eventType = EventType.CONFERENCE;
//	final UUID attendeeId = SqlDataStorage.ATTENDEE_ID;
//	final UUID organizerId = SqlDataStorage.ORGANIZER_ID;
//	final UUID sponsorId = SqlDataStorage.SPONSOR_ID;
//	final LocalDateTime localDateTime = LocalDateTime.now();
//	final Duration duration = Duration.ZERO;
//
//	final int limitOfPeople = 1000;
//	final EventDto dto = new EventDto(null, null, null, name, place, eventType, List.of(attendeeId), organizerId,
//		limitOfPeople, null, localDateTime, duration);
//
//	final ResponseEntity<EventDto> responseEntity = this.restTemplate
//		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, EventDto.class);
//
//	assertEquals(201, responseEntity.getStatusCode().value());
//
//	final EventDto actualEventDto = responseEntity.getBody();
//
//	final Integer updatedLimitOfPeople = 1800;
//	final String updatedName = this.generateString();
//	final String updatedPlace = this.generateString();
//	final Duration updatedDuration = Duration.ofHours(12);
//	final ResponseEntity<EventDto> editedResponseEntity = this.restTemplate.exchange(
//		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualEventDto.uuid().toString(), HttpMethod.PUT,
//		this.getHttpEntity(new EventDto(actualEventDto.uuid(), null, null, updatedName, updatedPlace, eventType,
//			List.of(attendeeId), organizerId, updatedLimitOfPeople, List.of(sponsorId), localDateTime,
//			updatedDuration)),
//		EventDto.class);
//
//	final EventDto actualEntity = editedResponseEntity.getBody();
//
//	assertEquals(actualEventDto.uuid(), actualEntity.uuid());
//	assertEquals(actualEventDto.createdAt(), actualEntity.createdAt());
//	assertEquals(updatedName, actualEntity.denomination());
//	assertEquals(updatedPlace, actualEntity.place());
//
//	assertEquals(List.of(attendeeId), actualEntity.attendeesIds());
//	assertEquals(organizerId, actualEntity.organizerId());
//	assertEquals(updatedLimitOfPeople, actualEntity.limitOfPeople());
//	assertEquals(localDateTime, actualEntity.startTimeOfEvent());
//	assertEquals(updatedDuration, actualEntity.duration());
//	assertEquals(List.of(sponsorId), actualEntity.sponsorsIds());
//
//	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualEntity.uuid(),
//		HttpMethod.DELETE, null, Void.class);
//
//	final ResponseEntity<EventDto> getDeletedEntity = this.restTemplate.getForEntity(
//		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualEntity.uuid(), EventDto.class);
//
//	assertTrue(getDeletedEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));
//
//    }
//
//    @Test
//    public void getEventWithId() {
//
//	final String name = this.generateString();
//	final String place = this.generateString();
//
//	final EventType eventType = EventType.CONFERENCE;
//	final UUID attendeeId = SqlDataStorage.ATTENDEE_ID;
//	final UUID organizerId = SqlDataStorage.ORGANIZER_ID;
//	final UUID sponsorId = SqlDataStorage.SPONSOR_ID;
//	final LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // work around because
//												 // entity returns a
//												 // less precise version
//	final Duration duration = Duration.ZERO;
//	final int limitOfPeople = 1000;
//
//	final EventDto dto = new EventDto(null, null, null, name, place, eventType, List.of(attendeeId), organizerId,
//		limitOfPeople, List.of(sponsorId), localDateTime, duration);
//
//	final ResponseEntity<EventDto> responseEntity = this.restTemplate
//		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, EventDto.class);
//
//	assertEquals(201, responseEntity.getStatusCode().value());
//
//	final EventDto actualEventDto = responseEntity.getBody();
//
//	final ResponseEntity<EventDto> getEntity = this.restTemplate.getForEntity(
//		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + actualEventDto.uuid(), EventDto.class);
//
//	assertEquals(actualEventDto, getEntity.getBody());
//
//    }
//
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    private HttpEntity getHttpEntity(final EventDto body) {
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
//}
