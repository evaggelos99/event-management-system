package org.com.ems.attendee.api.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.com.ems.attendee.api.Attendee;
import org.com.ems.attendee.api.AttendeeDto;

public final class AttendeeObjectGenerator {

    private AttendeeObjectGenerator() {

    }

    public static Attendee generateAttendee(final UUID... ticketIds) {

	final Instant now = Instant.now();
	return new Attendee(UUID.randomUUID(), now, now, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
		List.of(ticketIds));

    }

    public static AttendeeDto generateAttendeeDto(final UUID... ticketIds) {

	final List<UUID> listTickets = ticketIds != null ? List.of(ticketIds) : List.of();

	final Timestamp timestamp = Timestamp.from(Instant.now());
	return new AttendeeDto(UUID.randomUUID(), timestamp, timestamp, UUID.randomUUID().toString(),
		UUID.randomUUID().toString(), listTickets);

    }
}
