package org.com.ems.api.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class EventTest {

	@Test
	void testEquals() {

		EqualsVerifier.forClass(Event.class).verify();
	}

	@Test
	void testHashCode() {

		final UUID randomUUID = UUID.randomUUID();

		final Attendee attendee = new Attendee(randomUUID, "firstName", "lastName", new Ticket());
		final Organizer organizer = new Organizer(randomUUID, "name", "website", "description",
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE));

		final var lhs = new Event(randomUUID, "name", "place", EventType.OTHER, List.of(attendee), organizer, 35);

		final var rhs = new Event(randomUUID, "name", "place", EventType.OTHER, List.of(attendee), organizer, 35);

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

}
