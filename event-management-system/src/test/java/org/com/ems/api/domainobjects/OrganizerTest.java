package org.com.ems.api.domainobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class OrganizerTest {

	String[] ignoredFields = new String[] { "updatedTimestamp" };

	@Test
	void testEquals() {

		EqualsVerifier.forClass(Organizer.class).withIgnoredFields(this.ignoredFields).verify();
	}

	@Test
	void testHashCodeDescription() {

		final UUID randomUUID = UUID.randomUUID();

		final var lhs = new Organizer(randomUUID, "name", "website", "description",
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE), new ContactInformation("example@domain.com",
						70493729392L, "308 Negra Arroyo Lane, Albuquerque, New Mexico."));
		final var rhs = new Organizer(randomUUID, "name", "website", "description",
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE), new ContactInformation("example@domain.com",
						70493729392L, "308 Negra Arroyo Lane, Albuquerque, New Mexico."));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

	@Test
	void testHashCodeNullDescription() {

		final UUID randomUUID = UUID.randomUUID();

		final var lhs = new Organizer(randomUUID, "name", "website", null,
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE), new ContactInformation("example@domain.com",
						70493729392L, "308 Negra Arroyo Lane, Albuquerque, New Mexico."));
		final var rhs = new Organizer(randomUUID, "name", "website", null,
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE), new ContactInformation("example@domain.com",
						70493729392L, "308 Negra Arroyo Lane, Albuquerque, New Mexico."));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

}
