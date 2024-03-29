package org.com.ems.api.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class OrganizerTest {

	@Test
	void test() {

		EqualsVerifier.forClass(Organizer.class).verify();
	}

	@Test
	void testHashCodeDescription() {

		final var lhs = new Organizer("name", "website", "description",
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE));
		final var rhs = new Organizer("name", "website", "description",
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

	@Test
	void testHashCodeNullDescription() {

		final var lhs = new Organizer("name", "website", null, List.of(EventType.CONFERENCE, EventType.NIGHTLIFE));
		final var rhs = new Organizer("name", "website", null, List.of(EventType.CONFERENCE, EventType.NIGHTLIFE));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

}
