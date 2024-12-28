package io.github.evaggelos99.ems.organizer.api.util;

import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.github.evaggelos99.ems.organizer.api.OrganizerDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class OrganizerObjectGenerator {

    private OrganizerObjectGenerator() {

    }

    public static OrganizerDto generateOrganizerDto(final EventType... eventTypes) {

        final Instant timestamp = Instant.now();

        return new OrganizerDto(UUID.randomUUID(), timestamp, timestamp, UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), List.of(eventTypes),
                generateContactInformation());
    }

    public static Organizer generateOrganizer(final EventType... eventTypes) {

        final Instant now = Instant.now();
        return new Organizer(UUID.randomUUID(), now, now, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), List.of(eventTypes), generateContactInformation());
    }

    public static ContactInformation generateContactInformation() {

        return new ContactInformation(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString());
    }

    public static OrganizerDto generateOrganizerDtoWithoutTimestamps(final EventType... eventTypes) {

        return new OrganizerDto(UUID.randomUUID(), null, null, UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), List.of(eventTypes),
                generateContactInformation());
    }
}
