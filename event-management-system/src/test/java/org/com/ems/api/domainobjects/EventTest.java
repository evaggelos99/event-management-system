package org.com.ems.api.domainobjects;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class EventTest {

	String[] ignoredFields = new String[] { "updatedTimestamp" };

	@Test
	void testEquals() {

		EqualsVerifier.forClass(Event.class).withIgnoredFields(this.ignoredFields).verify();
	}

//	@Test
//	void testHashCode() {
//
//		final UUID randomUUID = UUID.randomUUID();
//		final UUID ticketUUID = UUID.randomUUID();
//
//		final Attendee attendee = new Attendee(randomUUID, "firstName", "lastName", List.of(ticketUUID));
//		final Organizer organizer = new Organizer(randomUUID, "name", "website", "description",
//				List.of(EventType.CONFERENCE, EventType.NIGHTLIFE), new ContactInformation("example@domain.com",
//						70493729392L, "308 Negra Arroyo Lane, Albuquerque, New Mexico."));
//
//		final Sponsor sponsor = new Sponsor(randomUUID, "name", "website", 50_000, new ContactInformation(
//				"example@domain.com", 70493729392L, "308 Negra Arroyo Lane, Albuquerque, New Mexico."));
//
//		final ZonedDateTime startTimeOfEvent = ZonedDateTime.now();
//		final Duration durationOfEvent = Duration.of(5, ChronoUnit.HOURS);
//
//		new Event(randomUUID, "name", "place", EventType.OTHER, List.of(randomUUID), randomUUID, Duration.ZERO,
//				ticketUUID, startTimeOfEvent, durationOfEvent);
//
//		final var rhs = new Event(randomUUID, "name", "place", EventType.OTHER, List.of(randomUUID), organizer, 35,
//				randomUUID, startTimeOfEvent, durationOfEvent);
//
//		assertEquals(lhs, rhs);
//		assertEquals(lhs.hashCode(), rhs.hashCode());
//	}

}
