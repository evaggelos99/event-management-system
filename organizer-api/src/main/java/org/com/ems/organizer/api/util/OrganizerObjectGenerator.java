package org.com.ems.organizer.api.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.com.ems.common.api.domainobjects.ContactInformation;
import org.com.ems.common.api.domainobjects.EventType;
import org.com.ems.organizer.api.Organizer;
import org.com.ems.organizer.api.OrganizerDto;

public final class OrganizerObjectGenerator {

    private OrganizerObjectGenerator() {

    }

    public static OrganizerDto generateOrganizerDto(final EventType... eventTypes) {

	final Timestamp timestamp = Timestamp.from(Instant.now());
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
}
