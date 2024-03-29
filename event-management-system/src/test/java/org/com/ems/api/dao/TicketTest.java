package org.com.ems.api.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class TicketTest {

	@Test
	void testEquals() {

		EqualsVerifier.forClass(Ticket.class).verify();
	}

	@Test
	void testHashCodeNullAttendeeId() {

		final UUID attendeeID = null;
		final UUID eventID = UUID.randomUUID();

		final var lhs = new Ticket(attendeeID, eventID, TicketType.GENERAL_ADMISSION, 50, true,
				new SeatingInformation("AB", "NORTH"));
		final var rhs = new Ticket(attendeeID, eventID, TicketType.GENERAL_ADMISSION, 50, true,
				new SeatingInformation("AB", "NORTH"));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

	@Test
	void testHashCodeAttendeeId() {

		final UUID attendeeID = UUID.randomUUID();
		final UUID eventID = UUID.randomUUID();

		final var lhs = new Attendee("firstName", "lastName", new Ticket(attendeeID, eventID,
				TicketType.GENERAL_ADMISSION, 50, true, new SeatingInformation("AB", "NORTH")));
		final var rhs = new Attendee("firstName", "lastName", new Ticket(attendeeID, eventID,
				TicketType.GENERAL_ADMISSION, 50, true, new SeatingInformation("AB", "NORTH")));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

}
