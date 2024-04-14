package org.com.ems.api.domainobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class AttendeeTest {

	String[] ignoredFields = new String[] { "updatedTimestamp" };

	@Test
	void testEquals() {

		EqualsVerifier.forClass(Attendee.class).withIgnoredFields(this.ignoredFields).verify();
	}

	@Test
	void testHashCodeTicket() {

		final UUID randomUUID = UUID.randomUUID();
		final UUID ticketUUID = UUID.randomUUID();
		final var lhs = new Attendee(randomUUID, "firstName", "lastName", List.of(ticketUUID));
		final var rhs = new Attendee(randomUUID, "firstName", "lastName", List.of(ticketUUID));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

	@Test
	void testHashCodeEmptyTicket() {

		final UUID randomUUID = UUID.randomUUID();
		final UUID eventUUID = UUID.randomUUID();
		final UUID ticketUUID = UUID.randomUUID();

		final var lhs = new Attendee(randomUUID, "firstName", "lastName", List.of());
		final var rhs = new Attendee(randomUUID, "firstName", "lastName", List.of());

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

}
