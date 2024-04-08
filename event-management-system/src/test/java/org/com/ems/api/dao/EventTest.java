package org.com.ems.api.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class EventTest {

	String[] ignoredFields = new String[] { "timestamp", "updatedOn" };

	@Test
	void testEquals() {

		EqualsVerifier.forClass(Event.class).withIgnoredFields(this.ignoredFields).verify();
	}

	@Test
	void testHashCode() {

		final UUID randomUUID = UUID.randomUUID();

		final Attendee attendee = new Attendee(randomUUID, "firstName", "lastName", new Ticket());
		final Organizer organizer = new Organizer(randomUUID, "name", "website", "description",
				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE), new ContactInformation("example@domain.com",
						70493729392L, "308 Negra Arroyo Lane, Albuquerque, New Mexico."));

		final Sponsor sponsor = new Sponsor(randomUUID, "name", "website", 50_000, new ContactInformation(
				"example@domain.com", 70493729392L, "308 Negra Arroyo Lane, Albuquerque, New Mexico."));

		final ZonedDateTime startTimeOfEvent = ZonedDateTime.now();
		final Duration durationOfEvent = Duration.of(5, ChronoUnit.HOURS);

		final var lhs = new Event(randomUUID, "name", "place", EventType.OTHER, List.of(attendee), organizer, 35,
				sponsor, startTimeOfEvent, durationOfEvent);

		final var rhs = new Event(randomUUID, "name", "place", EventType.OTHER, List.of(attendee), organizer, 35,
				sponsor, startTimeOfEvent, durationOfEvent);

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

}
