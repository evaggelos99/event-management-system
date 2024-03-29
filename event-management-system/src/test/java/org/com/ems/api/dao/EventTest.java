package org.com.ems.api.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class EventTest {

	@Test
	void testEquals() {

		EqualsVerifier.forClass(Event.class).verify();
	}

	@Test
	void testHashCode() {

		final Attendee attendee = new Attendee("firstName", "lastName", new Ticket());
		final Organizer organizer = new Organizer("name", "website", "description",
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE));

		final var lhs = new Event("name", "place", EventType.OTHER, List.of(attendee), organizer, 35);

		final var rhs = new Event("name", "place", EventType.OTHER, List.of(attendee), organizer, 35);

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

}
