package io.github.evaggelos99.ems.attendee.api.util;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class AttendeeObjectGenerator {

    private AttendeeObjectGenerator() {
    }

    public static Attendee generateAttendee(final UUID... ticketIds) {

        final Instant now = Instant.now();
        return new Attendee(UUID.randomUUID(), now, now, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                List.of(ticketIds));
    }

    public static AttendeeDto generateAttendeeDto(final UUID id, final UUID... ticketIds) {

        final List<UUID> listTickets = ticketIds != null ? List.of(ticketIds) : List.of();
        final Instant instantNow = Instant.now();
        return AttendeeDto.builder()
                .uuid(id != null ? id : UUID.randomUUID())
                .createdAt(instantNow)
                .lastUpdated(instantNow)
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .ticketIDs(listTickets)
                .build();
    }

    public static AttendeeDto generateAttendeeDtoWithoutTimestamps(final UUID uuid, final UUID... ticketIds) {

        final List<UUID> listTickets = ticketIds != null ? List.of(ticketIds) : List.of();
        return AttendeeDto.builder()
                .uuid(uuid != null ? uuid : UUID.randomUUID())
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .ticketIDs(listTickets)
                .build();
    }
}
