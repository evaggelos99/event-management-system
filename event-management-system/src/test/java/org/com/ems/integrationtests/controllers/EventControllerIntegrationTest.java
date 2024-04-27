package org.com.ems.integrationtests.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.com.ems.EventManagementSystemApplication;
import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.dto.EventDto;
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
class EventControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost";
    private static final String RELATIVE_ENDPOINT = "/event";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Order(value = 0)
    @Test
    public void postEventWithNotNullFields_thenExpectFieldsToNotBeNull() {

	final String name = "name";
	final String place = "place";

	final EventType eventType = EventType.CONFERENCE;
	final UUID attendeeId = UUID.randomUUID();
	final UUID organizerId = UUID.randomUUID();
	final UUID sponsorId = UUID.randomUUID();
	final LocalDateTime localDateTime = LocalDateTime.now();
	final Duration duration = Duration.ZERO;
	final int limitOfPeople = 1000;

	final EventDto dto = new EventDto(null, null, name, place, eventType, List.of(attendeeId), organizerId,
		limitOfPeople, sponsorId, localDateTime, duration);

	System.out.println(this.restTemplate);

	final ResponseEntity<EventDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, EventDto.class);

	System.out.println(responseEntity);

	assertEquals(201, responseEntity.getStatusCode().value());

	final EventDto actualEntity = responseEntity.getBody();

	assertNotNull(actualEntity.uuid());
	assertNotNull(actualEntity.lastUpdated());
	assertEquals(name, actualEntity.name());
	assertEquals(place, actualEntity.place());
	assertEquals(eventType, actualEntity.eventType());

	assertEquals(List.of(attendeeId), actualEntity.attendeesIDs());
	assertEquals(organizerId, actualEntity.organizerID());
	assertEquals(limitOfPeople, actualEntity.limitOfPeople());
	assertEquals(sponsorId, actualEntity.sponsorID());
	assertEquals(localDateTime, actualEntity.startTimeOfEvent());
	assertEquals(duration, actualEntity.durationOfEvent());

    }

    @Order(value = 1)
    @Test
    public void postEventWithNullFields_thenExpectFieldsToNotBeNull() {

	final String name = "name";
	final String place = "place";

	final EventType eventType = EventType.CONFERENCE;
	final UUID attendeeId = UUID.randomUUID();
	final UUID organizerId = UUID.randomUUID();
	final LocalDateTime localDateTime = LocalDateTime.now();
	final Duration duration = Duration.ZERO;
	final int limitOfPeople = 1000;

	final EventDto dto = new EventDto(null, null, name, place, eventType, List.of(attendeeId), organizerId,
		limitOfPeople, null, localDateTime, duration);

	final ResponseEntity<EventDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, EventDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final EventDto actualEntity = responseEntity.getBody();

	assertNotNull(actualEntity.uuid());
	assertNotNull(actualEntity.lastUpdated());
	assertEquals(name, actualEntity.name());
	assertEquals(place, actualEntity.place());
	assertEquals(eventType, actualEntity.eventType());

	assertEquals(List.of(attendeeId), actualEntity.attendeesIDs());
	assertEquals(organizerId, actualEntity.organizerID());
	assertEquals(limitOfPeople, actualEntity.limitOfPeople());
	assertNull(actualEntity.sponsorID());
	assertEquals(localDateTime, actualEntity.startTimeOfEvent());
	assertEquals(duration, actualEntity.durationOfEvent());

    }

    @Order(value = 2)
    @Test
    public void postEventWithNullFieldsThenEditEvent_thenExpectFieldsToNotBeNull() {

	final String name = "name";
	final String place = "place";

	final EventType eventType = EventType.CONFERENCE;
	final UUID attendeeId = UUID.randomUUID();
	final UUID organizerId = UUID.randomUUID();
	final UUID sponsorId = UUID.randomUUID();
	final LocalDateTime localDateTime = LocalDateTime.now();
	final Duration duration = Duration.ZERO;

	final int limitOfPeople = 1000;
	final EventDto dto = new EventDto(null, null, name, place, eventType, List.of(attendeeId), organizerId,
		limitOfPeople, null, localDateTime, duration);

	final ResponseEntity<EventDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, EventDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final EventDto entityDto = responseEntity.getBody();

	final ResponseEntity<EventDto> editedResponseEntity = this.restTemplate.exchange(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + entityDto.uuid().toString(), HttpMethod.PUT,
		this.getHttpEntity(new EventDto(entityDto.uuid(), null, name + place, place + name, eventType,
			List.of(attendeeId), organizerId, limitOfPeople, sponsorId, localDateTime, duration)),
		EventDto.class);

	final EventDto actualEntity = editedResponseEntity.getBody();

	assertEquals(entityDto.uuid(), actualEntity.uuid());
	assertTrue(actualEntity.lastUpdated().isAfter(entityDto.lastUpdated()));
	assertEquals(name + place, actualEntity.name());
	assertEquals(place + name, actualEntity.place());

	assertEquals(List.of(attendeeId), actualEntity.attendeesIDs());
	assertEquals(organizerId, actualEntity.organizerID());
	assertEquals(limitOfPeople, actualEntity.limitOfPeople());
	assertEquals(localDateTime, actualEntity.startTimeOfEvent());
	assertEquals(duration, actualEntity.durationOfEvent());
	assertEquals(sponsorId, actualEntity.sponsorID());

    }

    @Order(value = 3)
    @Test
    public void getEvents_thenExpectToHave3InTheDb() {

	@SuppressWarnings("rawtypes")
	final ResponseEntity<Collection> responseEntity = this.restTemplate
		.getForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, Collection.class);

	assertEquals(3, responseEntity.getBody().size());

    }

    @Order(value = 4)
    @Test
    public void getEventWithUUID_thenExpectToReturnTheSameExactObject() {

	final String name = "name";
	final String place = "place";

	final EventType eventType = EventType.CONFERENCE;
	final UUID attendeeId = UUID.randomUUID();
	final UUID organizerId = UUID.randomUUID();
	final UUID sponsorId = UUID.randomUUID();
	final LocalDateTime localDateTime = LocalDateTime.now();
	final Duration duration = Duration.ZERO;
	final int limitOfPeople = 1000;

	final EventDto dto = new EventDto(null, null, name, place, eventType, List.of(attendeeId), organizerId,
		limitOfPeople, sponsorId, localDateTime, duration);

	final ResponseEntity<EventDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, EventDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final EventDto expectedEntity = responseEntity.getBody();

	final ResponseEntity<EventDto> getEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), EventDto.class);

	assertEquals(expectedEntity, getEntity.getBody());

    }

    @Order(value = 5)
    @Test
    public void deleteEventWithUUID_thenExpectForTheObjectTobeDeleted() {

	final String name = "name";
	final String place = "place";

	final EventType eventType = EventType.CONFERENCE;
	final UUID attendeeId = UUID.randomUUID();
	final UUID organizerId = UUID.randomUUID();
	final UUID sponsorId = UUID.randomUUID();
	final LocalDateTime localDateTime = LocalDateTime.now();
	final Duration duration = Duration.ZERO;
	final int limitOfPeople = 1000;

	final EventDto dto = new EventDto(null, null, name, place, eventType, List.of(attendeeId), organizerId,
		limitOfPeople, sponsorId, localDateTime, duration);

	final ResponseEntity<EventDto> responseEntity = this.restTemplate
		.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, EventDto.class);

	assertEquals(201, responseEntity.getStatusCode().value());

	final EventDto expectedEntity = responseEntity.getBody();

	this.restTemplate.exchange(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(),
		HttpMethod.DELETE, null, Void.class);

	final ResponseEntity<EventDto> getEntity = this.restTemplate.getForEntity(
		HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT + "/" + expectedEntity.uuid(), EventDto.class);

	assertTrue(getEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404)));

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private HttpEntity getHttpEntity(final EventDto body) {

	return new HttpEntity(body);

    }
}
