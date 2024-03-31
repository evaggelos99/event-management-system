package org.com.ems.api.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class OrganizerTest {

	@Test
	void test() {

		EqualsVerifier.forClass(Organizer.class).verify();
	}

	@Test
	void testHashCodeDescription() {

		final UUID randomUUID = UUID.randomUUID();

		final var lhs = new Organizer(randomUUID, "name", "website", "description",
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE));
		final var rhs = new Organizer(randomUUID, "name", "website", "description",
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

	@Test
	void testHashCodeNullDescription() {

		final UUID randomUUID = UUID.randomUUID();

		final var lhs = new Organizer(randomUUID, "name", "website", null,
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE));
		final var rhs = new Organizer(randomUUID, "name", "website", null,
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

}
