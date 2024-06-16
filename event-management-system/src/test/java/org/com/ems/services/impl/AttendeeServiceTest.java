package org.com.ems.services.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.converters.AttendeeToAttendeeDtoConverter;
import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.domainobjects.Ticket;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

    private AttendeeService service;

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

	final Mono<Attendee> monoAttendee = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	StepVerifier.create(monoAttendee).assertNext(attendee -> {

	    Assertions.assertEquals(dto.uuid(), attendee.getUuid());
	    Assertions.assertEquals(dto.firstName(), attendee.getFirstName());
	    Assertions.assertEquals(dto.lastName(), attendee.getLastName());
	    Assertions.assertEquals(dto.ticketIDs(), attendee.getTicketIDs());
	}).verifyComplete();

	StepVerifier.create(Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()))).expectNext(true)
		.verifyComplete();

	StepVerifier.create(this.service.existsById(expectedUUID)).expectNext(false).verifyComplete();

    }

    @Test
    void add_get_delete_whenInvokedWithAttendeeDtoThenExpectToBeSaved_expectThatCanBeFetched() {

	final String firstName = this.generateString();
	final String lastName = this.generateString();
	final UUID expectedUUID = UUID.randomUUID();

	final AttendeeDto dto = new AttendeeDto(UUID.randomUUID(), Timestamp.from(Instant.now()),
		Timestamp.from(Instant.now()), firstName, lastName, List.of(expectedUUID));

	final Mono<Attendee> monoAttendee = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	StepVerifier.create(monoAttendee.flatMap(attendee -> this.service.get(attendee.getUuid())))
		.assertNext(Assertions::assertNotNull);

	StepVerifier.create(Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()))).expectNext(true);

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

	final Mono<Attendee> monoAttendee = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	final UUID updatedTicketId = UUID.randomUUID();
	final String updatedFirstName = UUID.randomUUID().toString();
	final String updatedLastName = UUID.randomUUID().toString();

	final Timestamp updatedTimestamp = Timestamp.from(Instant.now());
	final AttendeeDto newDto = new AttendeeDto(attendeeId, createdAt, updatedTimestamp, updatedFirstName,
		updatedLastName, List.of(updatedTicketId));

	final Mono<Attendee> monoUpdatedAttendee = monoAttendee
		.flatMap(attendee -> this.service.edit(attendee.getUuid(), newDto));

	StepVerifier.create(monoUpdatedAttendee).assertNext(updatedAttendee -> {

	    Assertions.assertEquals(List.of(updatedTicketId), updatedAttendee.getTicketIDs());
	    Assertions.assertEquals(updatedFirstName, updatedAttendee.getFirstName());
	    Assertions.assertEquals(updatedLastName, updatedAttendee.getLastName());
	    Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));
	}).verifyComplete();

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

	StepVerifier.create(Assertions.assertDoesNotThrow(() -> this.service.add(dto))).assertNext(x -> {

	    Assertions.assertEquals(dto.uuid(), x.getUuid());
	});

	StepVerifier.create(Assertions.assertDoesNotThrow(() -> this.service.add(dto2))).assertNext(x -> {

	    Assertions.assertEquals(dto2.uuid(), x.getUuid());
	});

	final Flux<Attendee> allAttendees = Assertions.assertDoesNotThrow(() -> this.service.getAll());

	StepVerifier.create(allAttendees).assertNext(Assertions::assertNotNull).assertNext(Assertions::assertNotNull)
		.verifyComplete();

	final Mono<
		Boolean> firstAttendeeDeletion = Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));
	final Mono<
		Boolean> secondAttendeeDeletion = Assertions.assertDoesNotThrow(() -> this.service.delete(dto2.uuid()));

	StepVerifier.create(firstAttendeeDeletion).expectNext(true);
	StepVerifier.create(secondAttendeeDeletion).expectNext(true);

    }

    @Test
    void add_addTicket_delete_whenInvokedWithAttendeeDtoAndValidTicket_thenExpectToBeSaved_thenExpectTicketToBeAdded_thenDeleteAttendee() {

	final String firstName = this.generateString();
	final String lastName = this.generateString();
	final UUID ticketId = UUID.randomUUID();

	final UUID attendeeId = UUID.randomUUID();
	final Timestamp createdAt = Timestamp.from(Instant.now());
	final AttendeeDto dto = new AttendeeDto(attendeeId, createdAt, createdAt, firstName, lastName, List.of());

	Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	StepVerifier.create(this.service.addTicket(dto.uuid(), ticketId)).expectNext(true);

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

    @Test
    void add_addTicket_delete_whenInvokedWithAttendeeDtoAndValidTicket_thenExpectToBeSaved_thenExpectTicketToBeNotBeAdded_thenDeleteAttendee() {

	final String firstName = this.generateString();
	final String lastName = this.generateString();
	final UUID ticketId = UUID.randomUUID();

	final UUID attendeeId = UUID.randomUUID();
	final Timestamp createdAt = Timestamp.from(Instant.now());
	final AttendeeDto dto = new AttendeeDto(attendeeId, createdAt, createdAt, firstName, lastName, List.of());

	Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	StepVerifier.create(this.service.addTicket(dto.uuid(), ticketId)).expectNext(false);

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

    private String generateString() {

	return UUID.randomUUID().toString();

    }

}
