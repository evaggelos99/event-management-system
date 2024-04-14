package org.com.ems.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IAttendeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AttendeeControllerTest {

	@Mock
	private IAttendeeRepository repositoryMock;

	private AttendeeController controller;

	@BeforeEach
	void setUp() {

		this.controller = new AttendeeController(this.repositoryMock);
	}

	@Test
	void postAttendee_givenValidDao_expectToReturnTheSameDaoObject() {

		final UUID randomUUID = UUID.randomUUID();
		final UUID eventID = UUID.randomUUID();
		final UUID ticketUUID = UUID.randomUUID();

		final var expectedAttendee = new Attendee(randomUUID, "firstName", "lastName", List.of(ticketUUID));

		Mockito.when(this.repositoryMock.save(Mockito.any(Attendee.class))).thenAnswer(x -> x.getArguments()[0]);

		final Attendee actualAttendee = this.controller.postAttendee(expectedAttendee);

		assertEquals(expectedAttendee, actualAttendee);
	}

	@Test
	void getAttendee_givenValidUUID_expectToReturnTheCorrectDaoObject() {

		final UUID attendeeId = UUID.randomUUID();
		final UUID ticketId = UUID.randomUUID();

		final var expectedAttendee = new Attendee(attendeeId, "firstName", "lastName", List.of(ticketId));

		Mockito.when(this.repositoryMock.findById(attendeeId)).thenReturn(Optional.ofNullable(expectedAttendee));

		final Attendee actualAttendee = this.controller.getAttendee(attendeeId.toString());

		assertEquals(expectedAttendee, actualAttendee);
	}

	@Test
	void getAttendee_giveUUIDThatCannotBeFound_expectThrowException() {

		final UUID attendeeId = UUID.randomUUID();

		Mockito.when(this.repositoryMock.findById(attendeeId)).thenReturn(Optional.empty());

		assertThrows(ObjectNotFoundException.class, () -> this.controller.getAttendee(attendeeId.toString()));
	}

	@Test
	void putAttendee_givenValidDao_expectToReturnTheSameDaoObject() {

		final UUID attendeeId = UUID.randomUUID();
		final UUID eventID = UUID.randomUUID();
		final UUID ticketId = UUID.randomUUID();

		final var expectedAttendee = new Attendee(attendeeId, "firstName", "lastName", List.of(ticketId));

		final UUID expectedUuid = expectedAttendee.getUuid();

		Mockito.when(this.repositoryMock.existsById(Mockito.any(UUID.class))).thenReturn(true);

		Mockito.when(this.repositoryMock.save(Mockito.any(Attendee.class))).thenReturn(expectedAttendee);

		final Attendee actualAttendee = this.controller.putAttendee(expectedUuid.toString(), expectedAttendee);

		assertEquals(expectedAttendee, actualAttendee);
	}

	@Test
	void putAttendee_givenValidDaoButUuidDoesNotMatch_expectToThrowException() {

		final UUID attendeeID = UUID.randomUUID();
		final UUID diffId = UUID.randomUUID();
		final UUID ticketID = UUID.randomUUID();

		final var expectedAttendee = new Attendee(attendeeID, "firstName", "lastName", List.of(ticketID));

		Mockito.when(this.repositoryMock.existsById(diffId)).thenReturn(false);

		assertThrows(ObjectNotFoundException.class,
				() -> this.controller.putAttendee(diffId.toString(), expectedAttendee));
	}

	@Test
	void deleteAttendee_givenValidUuidMatchesObjet_expectToNotThrowException() {

		final UUID attendeeId = UUID.randomUUID();

		Assertions.assertDoesNotThrow(() -> this.controller.deleteAttendee(attendeeId.toString()));
	}

	@Test
	void getAttendees_expectReturnAllObjects() {

		final UUID attendeeId = UUID.randomUUID();
		final UUID attendeeId2 = UUID.randomUUID();
		final UUID ticketId = UUID.randomUUID();

		final var expectedAttendee = new Attendee(attendeeId, "firstName", "lastName", List.of(ticketId));

		final var expectedAttendee2 = new Attendee(attendeeId2, "firstName", "lastName", List.of(ticketId));

		Mockito.when(this.repositoryMock.findAll()).thenReturn(List.of(expectedAttendee, expectedAttendee2));

		assertEquals(List.of(expectedAttendee, expectedAttendee2), this.controller.getAttendees());
	}

}
