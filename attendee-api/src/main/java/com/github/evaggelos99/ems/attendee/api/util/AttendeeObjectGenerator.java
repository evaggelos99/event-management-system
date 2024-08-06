package com.github.evaggelos99.ems.attendee.api.util;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.github.evaggelos99.ems.attendee.api.Attendee;
import com.github.evaggelos99.ems.attendee.api.AttendeeDto;

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
		final Instant instantNow = Instant.now();
		return new AttendeeDto(UUID.randomUUID(), instantNow, instantNow, UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), listTickets);
	}

	public static AttendeeDto generateAttendeeDtoWithoutTimestamps(final UUID... ticketIds) {

		final List<UUID> listTickets = ticketIds != null ? List.of(ticketIds) : List.of();
		return new AttendeeDto(UUID.randomUUID(), null, null, UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), listTickets);
	}
}
