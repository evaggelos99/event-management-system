package org.com.ems.services.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.converters.AttendeeToAttendeeDtoConverter;
import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.domainobjects.SeatingInformation;
import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.domainobjects.TicketType;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.db.IAttendeeRepository;
import org.com.ems.services.api.IEventService;
import org.com.ems.services.api.ILookUpService;
import org.com.ems.util.SpringTestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@ContextConfiguration(classes = { SpringTestConfiguration.class })
@ActiveProfiles("service-tests")
class AttendeeServiceTest {

    @Autowired
    IAttendeeRepository attendeeRepository;
    Function<Attendee, AttendeeDto> attendeeToAttendeeDtoConverter = new AttendeeToAttendeeDtoConverter();
    @Mock
    IEventService eventServiceMock;
    @Mock
    ILookUpService<Ticket> lookUpTicketServiceMock;

    AttendeeService service;

    @BeforeEach
    void setUp() {

	this.service = new AttendeeService(this.attendeeRepository, this.attendeeToAttendeeDtoConverter,
		this.eventServiceMock, this.lookUpTicketServiceMock);

    }

    @Test
    void add_delete_existsById__whenInvokedWithAttendeeDtoThenExpectToBeSaved_thenExpectToBeDeleted() {

	final String firstName = this.generateString();
	final String lastName = this.generateString();
	final UUID expectedUUID = UUID.randomUUID();

	final AttendeeDto dto = new AttendeeDto(UUID.randomUUID(), Timestamp.from(Instant.now()),
		Timestamp.from(Instant.now()), firstName, lastName, List.of(expectedUUID));

	final Attendee attendee = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	Assertions.assertEquals(dto.uuid(), attendee.getUuid());
	Assertions.assertEquals(dto.firstName(), attendee.getFirstName());
	Assertions.assertEquals(dto.lastName(), attendee.getLastName());
	Assertions.assertEquals(dto.ticketIDs(), attendee.getTicketIDs());

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));
	Assertions.assertFalse(this.service.existsById(expectedUUID));

    }

    @Test
    void add_get_delete_whenInvokedWithAttendeeDtoThenExpectToBeSaved_expectThatCanBeFetched() {

	final String firstName = this.generateString();
	final String lastName = this.generateString();
	final UUID expectedUUID = UUID.randomUUID();

	final AttendeeDto dto = new AttendeeDto(UUID.randomUUID(), Timestamp.from(Instant.now()),
		Timestamp.from(Instant.now()), firstName, lastName, List.of(expectedUUID));

	final Attendee attendee = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	Assertions.assertTrue(this.service.get(attendee.getUuid()).isPresent());
	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

    @Test
    void add_edit_delete_whenInvokedWithAttendeeDto_thenExpectToBeSaved_thenEdited_thenDeleted() {

	final String firstName = this.generateString();
	final String lastName = this.generateString();
	final UUID expectedUUID = UUID.randomUUID();

	final UUID attendeeId = UUID.randomUUID();
	final Timestamp createdAt = Timestamp.from(Instant.now());
	final AttendeeDto dto = new AttendeeDto(attendeeId, createdAt, createdAt, firstName, lastName,
		List.of(expectedUUID));

	final Attendee attendee = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	final UUID updatedTicketId = UUID.randomUUID();
	final String updatedFirstName = UUID.randomUUID().toString();
	final String updatedLastName = UUID.randomUUID().toString();

	final Timestamp updatedTimestamp = Timestamp.from(Instant.now());
	final AttendeeDto newDto = new AttendeeDto(attendeeId, createdAt, updatedTimestamp, updatedFirstName,
		updatedLastName, List.of(updatedTicketId));

	final Attendee updatedAttendee = Assertions
		.assertDoesNotThrow(() -> this.service.edit(attendee.getUuid(), newDto));
	Assertions.assertEquals(List.of(updatedTicketId), updatedAttendee.getTicketIDs());
	Assertions.assertEquals(updatedFirstName, updatedAttendee.getFirstName());
	Assertions.assertEquals(updatedLastName, updatedAttendee.getLastName());
	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

    @Test
    void add_add_getAll_delete_delete_whenInvokedWithAttendeeDtos_thenExpectToBeSavedTwoTimes_thengetAllAttendees_thenDeleteTwo() {

	final String firstName = this.generateString();
	final String lastName = this.generateString();
	final UUID expectedUUID = UUID.randomUUID();

	final UUID attendeeId = UUID.randomUUID();
	final Timestamp createdAt = Timestamp.from(Instant.now());
	final AttendeeDto dto = new AttendeeDto(attendeeId, createdAt, createdAt, firstName, lastName,
		List.of(expectedUUID));

	final String firstName2 = this.generateString();
	final String lastName2 = this.generateString();
	final UUID expectedUUID2 = UUID.randomUUID();

	final UUID attendeeId2 = UUID.randomUUID();
	final AttendeeDto dto2 = new AttendeeDto(attendeeId2, createdAt, createdAt, firstName2, lastName2,
		List.of(expectedUUID2));

	Assertions.assertDoesNotThrow(() -> this.service.add(dto));
	Assertions.assertDoesNotThrow(() -> this.service.add(dto2));

	final Collection<Attendee> allAttendees = Assertions.assertDoesNotThrow(() -> this.service.getAll());

	Assertions.assertEquals(2, allAttendees.size());

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));
	Assertions.assertDoesNotThrow(() -> this.service.delete(dto2.uuid()));

    }

    @Test
    void add_addTicket_delete_whenInvokedWithAttendeeDtoAndValidTicket_thenExpectToBeSaved_thenExpectTicketToBeAdded_thenDeleteAttendee() {

	final String firstName = this.generateString();
	final String lastName = this.generateString();
	final UUID ticketId = UUID.randomUUID();

	final UUID attendeeId = UUID.randomUUID();
	final Timestamp createdAt = Timestamp.from(Instant.now());
	final AttendeeDto dto = new AttendeeDto(attendeeId, createdAt, createdAt, firstName, lastName, List.of());

	final Ticket ticket = new Ticket(UUID.randomUUID(), Instant.now(), Instant.now(), UUID.randomUUID(),
		TicketType.STUDENT, 50, true, new SeatingInformation("a", "a"));

	Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	Mockito.when(this.eventServiceMock.addAttendee(Mockito.any(), Mockito.eq(attendeeId))).thenReturn(true);

	Mockito.when(this.lookUpTicketServiceMock.get(ticketId)).thenReturn(Optional.of(ticket));

	Assertions.assertTrue(this.service.addTicket(dto.uuid(), ticketId));

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

//    @Test
//    void add_addTicket_delete_whenInvokedWithAttendeeDtoAndInvalidTicket_thenExpectToBeSaved_thenExpectTicketToNotBeAdded_thenDeleteAttendee() {
//
//	final String firstName = this.generateString();
//	final String lastName = this.generateString();
//	final UUID ticketId = UUID.randomUUID();
//
//	final UUID attendeeId = UUID.randomUUID();
//	final Timestamp createdAt = Timestamp.from(Instant.now());
//	final AttendeeDto dto = new AttendeeDto(attendeeId, createdAt, createdAt, firstName, lastName, List.of());
//
//	Assertions.assertDoesNotThrow(() -> this.service.add(dto));
//
//	Mockito.when(this.lookUpTicketServiceMock.get(ticketId)).thenReturn(Optional.empty());
//	Assertions.assertThrows(ObjectNotFoundException.class, () -> this.service.addTicket(dto.uuid(), ticketId));
//
//	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));
//
//    }

    private String generateString() {

	return UUID.randomUUID().toString();

    }

}
