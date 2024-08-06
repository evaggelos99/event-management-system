package com.github.evaggelos99.ems.organizer.api.util;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import com.github.evaggelos99.ems.common.api.domainobjects.EventType;
import com.github.evaggelos99.ems.organizer.api.Organizer;
import com.github.evaggelos99.ems.organizer.api.OrganizerDto;

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