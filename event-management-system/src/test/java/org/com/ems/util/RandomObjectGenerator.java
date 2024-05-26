package org.com.ems.util;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.api.domainobjects.SeatingInformation;
import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.domainobjects.TicketType;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.api.dto.EventDto;
import org.com.ems.api.dto.OrganizerDto;
import org.com.ems.api.dto.SponsorDto;
import org.com.ems.api.dto.TicketDto;

public final class RandomObjectGenerator {

    private static final List<TicketType> ALL_TICKET_TYPES = List.of(TicketType.values());
    private static final List<EventType> ALL_EVENT_TYPES = List.of(EventType.values());
    private static final Random RANDOM = new Random();

    private RandomObjectGenerator() {

    }

    public static Ticket generateTicket(final UUID eventId) {

	final TicketType randomTicketType = ALL_TICKET_TYPES.get(RANDOM.nextInt(ALL_TICKET_TYPES.size()));

	final Instant now = Instant.now();
	return new Ticket(UUID.randomUUID(), now, now, eventId, randomTicketType, RANDOM.nextInt(500),
		RANDOM.nextBoolean(), generateSeatingInformation());

    }

    public static SeatingInformation generateSeatingInformation() {

	return new SeatingInformation(UUID.randomUUID().toString(), UUID.randomUUID().toString());

    }

    public static Attendee generateAttendee(final UUID... ticketIds) {

	final Instant now = Instant.now();
	return new Attendee(UUID.randomUUID(), now, now, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
		List.of(ticketIds));

    }

    public static ContactInformation generateContactInformation() {

	return new ContactInformation(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
		UUID.randomUUID().toString());

    }

    public static Sponsor generateSponsor() {

	final Instant now = Instant.now();
	return new Sponsor(UUID.randomUUID(), now, now, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
		RANDOM.nextInt(500), generateContactInformation());

    }

    public static Organizer generateOrganizer(final EventType... eventTypes) {

	final Instant now = Instant.now();
	return new Organizer(UUID.randomUUID(), now, now, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
		UUID.randomUUID().toString(), List.of(eventTypes), generateContactInformation());

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

    public static TicketDto generateTicketDto(final UUID eventId) {

	final TicketType randomTicketType = ALL_TICKET_TYPES.get(RANDOM.nextInt(ALL_TICKET_TYPES.size()));

	final Timestamp timestamp = Timestamp.from(Instant.now());
	return new TicketDto(UUID.randomUUID(), timestamp, timestamp, eventId, randomTicketType, RANDOM.nextInt(500),
		RANDOM.nextBoolean(), generateSeatingInformation());

    }

    public static AttendeeDto generateAttendeeDto(final UUID... ticketIds) {

	final Timestamp timestamp = Timestamp.from(Instant.now());
	return new AttendeeDto(UUID.randomUUID(), timestamp, timestamp, UUID.randomUUID().toString(),
		UUID.randomUUID().toString(), List.of(ticketIds));

    }

    public static SponsorDto generateSponsorDto() {

	final Timestamp timestamp = Timestamp.from(Instant.now());
	return new SponsorDto(UUID.randomUUID(), timestamp, timestamp, UUID.randomUUID().toString(),
		UUID.randomUUID().toString(), RANDOM.nextInt(500), generateContactInformation());

    }

    public static OrganizerDto generateOrganizerDto(final EventType... eventTypes) {

	final Timestamp timestamp = Timestamp.from(Instant.now());
	return new OrganizerDto(UUID.randomUUID(), timestamp, timestamp, UUID.randomUUID().toString(),
		UUID.randomUUID().toString(), UUID.randomUUID().toString(), List.of(eventTypes),
		generateContactInformation());

    }

    public static EventDto generateEventDto(final UUID attendeeId,
					    final UUID organizerId,
					    final UUID sponsorId) {

	final EventType randomTicketType = ALL_EVENT_TYPES.get(RANDOM.nextInt(ALL_EVENT_TYPES.size()));

	final Timestamp timestamp = Timestamp.from(Instant.now());

	final List<UUID> listSponsors = sponsorId != null ? List.of(sponsorId) : List.of();

	final List<UUID> listAttendees = attendeeId != null ? List.of(attendeeId) : List.of();

	return new EventDto(UUID.randomUUID(), timestamp, timestamp, UUID.randomUUID().toString(),
		UUID.randomUUID().toString(), randomTicketType, listAttendees, organizerId, RANDOM.nextInt(1500),
		listSponsors, LocalDateTime.now().plus(5, ChronoUnit.DAYS), Duration.ofHours(5));

    }

}
