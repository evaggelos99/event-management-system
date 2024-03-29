package org.com.ems.api.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class AttendeeTest {

	@Test
	void testEquals() {

		EqualsVerifier.forClass(Attendee.class).verify();
	}

	@Test
	void testHashCodeEmptyTicket() {

		final var lhs = new Attendee("firstName", "lastName", new Ticket());
		final var rhs = new Attendee("firstName", "lastName", new Ticket());

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

	@Test
	void testHashCodeTicket() {

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
