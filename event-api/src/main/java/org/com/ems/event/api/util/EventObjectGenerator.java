package org.com.ems.event.api.util;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.com.ems.common.api.domainobjects.EventType;
import org.com.ems.event.api.Event;
import org.com.ems.event.api.EventDto;

public final class EventObjectGenerator {

    private EventObjectGenerator() {

    }

    private static final List<EventType> ALL_EVENT_TYPES = List.of(EventType.values());
    private static final Random RANDOM = new Random();

    public static EventDto generateEventDto(final UUID attendeeId,
					    final UUID organizerId,
					    final UUID sponsorId) {

	final EventType randomTicketType = ALL_EVENT_TYPES.get(RANDOM.nextInt(ALL_EVENT_TYPES.size()));

	final Timestamp timestamp = Timestamp.from(Instant.now());

	final List<UUID> listSponsors = sponsorId != null ? List.of(sponsorId) : List.of();

	final List<UUID> listAttendees = attendeeId != null ? List.of(attendeeId) : List.of();

	return new EventDto(UUID.randomUUID(), timestamp, timestamp, UUID.randomUUID().toString(),
		UUID.randomUUID().toString(), randomTicketType, listAttendees, organizerId, RANDOM.nextInt(1500),
		listSponsors, LocalDateTime.now().plus(5, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS),
		Duration.ofHours(5));

    }

    public static Event generateEvent(final UUID attendeeId,
				      final UUID organizerId,
				      final UUID sponsorId) {

	final EventType randomTicketType = ALL_EVENT_TYPES.get(RANDOM.nextInt(ALL_EVENT_TYPES.size()));

	final Instant now = Instant.now();
	return new Event(UUID.randomUUID(), now, now, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
		randomTicketType, List.of(attendeeId), organizerId, RANDOM.nextInt(1500), List.of(sponsorId),
		LocalDateTime.now().plus(5, ChronoUnit.DAYS), Duration.ofHours(5));

    }

}
