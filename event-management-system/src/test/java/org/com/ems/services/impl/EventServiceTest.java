package org.com.ems.services.impl;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.converters.EventToEventDtoConverter;
import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IEventRepository;
import org.com.ems.util.RandomObjectGenerator;
import org.com.ems.util.SpringTestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfiguration.class })
@ActiveProfiles("service-tests")
class EventServiceTest {

    @Autowired
    IEventRepository eventRepository;
    final Function<Event, EventDto> eventToEventDtoConverter = new EventToEventDtoConverter();

    private EventService service;

    @BeforeEach
    void setUp() throws Exception {

	this.service = new EventService(this.eventRepository, this.eventToEventDtoConverter);

    }

    @Test
    void add_get_getAll_existsById_delete_existsById_invokedWithValidDto_thenExpectToBeAdded_thenExpectItCanBeFetched_thenExpectToFetchAll_thenExpectItExists_thenExpectItCanBeDeleted() {

	final EventDto dto = RandomObjectGenerator.generateEventDto(UUID.randomUUID(), UUID.randomUUID(), null);

	final Event event = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	Assertions.assertEquals(dto.uuid(), event.getUuid());
	Assertions.assertEquals(dto.denomination(), event.getDenomination());
	Assertions.assertEquals(dto.place(), event.getPlace());
	Assertions.assertEquals(dto.eventType(), event.getEventType());
	Assertions.assertEquals(dto.attendeesIds(), event.getAttendeesIDs());
	Assertions.assertEquals(dto.organizerId(), event.getOrganizerID());
	Assertions.assertEquals(dto.limitOfPeople(), event.getLimitOfPeople());
	Assertions.assertEquals(dto.sponsorsIds(), event.getSponsorsIds());
	Assertions.assertEquals(dto.startTimeOfEvent(), event.getStartTime());
	Assertions.assertEquals(dto.duration(), event.getDuration());

	final Optional<Event> optionalEvent = this.service.get(dto.uuid());

	Assertions.assertEquals(event, optionalEvent.orElseThrow(() -> new AssertionError("Optional is null")));

	Assertions.assertEquals(1, this.service.getAll().size());

	Assertions.assertTrue(() -> this.service.existsById(dto.uuid()));

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

    @Test
    void add_addAttendee_delete_invokedWithValidDtoAndValidAttendeeId_thenExpectEventToBeSaved_thenExpectAttendeeToBeAdded_thenExpectItCanBeDeleted() {

	final EventDto dto = RandomObjectGenerator.generateEventDto(null, UUID.randomUUID(), null);
	Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	final UUID attendeeId = UUID.randomUUID();

	Assertions.assertTrue(() -> this.service.addAttendee(dto.uuid(), attendeeId));

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

    @Test
    void existsById_addAttendee_invokedWithInvalidEventId__thenExpectToNotExist_thenExpectAttendeeToNotBeAdded() {

	final UUID eventId = UUID.randomUUID();
	Assertions.assertFalse(() -> this.service.existsById(eventId));

	Assertions.assertThrows(ObjectNotFoundException.class,
		() -> this.service.addAttendee(eventId, UUID.randomUUID()));

    }

    @Test
    void add_edit_delete_invokedWithValidDto_thenExpectToBeAdded_thenExpectItCanBeFetched_thenExpectToFetchAll_thenExpectItExists_thenExpectItCanBeDeleted() {

	final UUID organizerId = UUID.randomUUID();
	final EventDto dto = RandomObjectGenerator.generateEventDto(null, organizerId, null);

	final Event event = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	final EventDto newDto = RandomObjectGenerator.generateEventDto(null, organizerId, null);

	final Event newEvent = Assertions.assertDoesNotThrow(() -> this.service.edit(dto.uuid(), newDto));

	Assertions.assertNotEquals(event, newEvent);

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

}
