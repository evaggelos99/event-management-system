package org.com.ems.api.domainobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.domainobjects.SeatingInformation;
import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.domainobjects.TicketType;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class TicketTest {

	String[] ignoredFields = new String[] { "timestamp", "updatedOn" };

	@Test
	void testEquals() {

		EqualsVerifier.forClass(Ticket.class).withIgnoredFields(this.ignoredFields).verify();
	}

	@Test
	void testHashCodeNullAttendeeId() {

		final UUID randomUUID = UUID.randomUUID();
		final UUID attendeeID = null;
		final UUID eventID = UUID.randomUUID();

		final var lhs = new Ticket(randomUUID, attendeeID, eventID, TicketType.GENERAL_ADMISSION, 50, true,
				new SeatingInformation("AB", "NORTH"));
		final var rhs = new Ticket(randomUUID, attendeeID, eventID, TicketType.GENERAL_ADMISSION, 50, true,
				new SeatingInformation("AB", "NORTH"));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

	@Test
	void testHashCodeAttendeeId() {

		final UUID randomUUID = UUID.randomUUID();
		final UUID attendeeID = UUID.randomUUID();
		final UUID eventID = UUID.randomUUID();

		final var lhs = new Attendee(randomUUID, "firstName", "lastName", new Ticket(randomUUID, attendeeID, eventID,
				TicketType.GENERAL_ADMISSION, 50, true, new SeatingInformation("AB", "NORTH")));
		final var rhs = new Attendee(randomUUID, "firstName", "lastName", new Ticket(randomUUID, attendeeID, eventID,
				TicketType.GENERAL_ADMISSION, 50, true, new SeatingInformation("AB", "NORTH")));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

}
