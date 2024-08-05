package org.com.ems.sponsor.api.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import org.com.ems.common.api.domainobjects.ContactInformation;
import org.com.ems.sponsor.api.Sponsor;
import org.com.ems.sponsor.api.SponsorDto;

public final class SponsorObjectGenerator {

	private static final Random RANDOM = new Random();

	private SponsorObjectGenerator() {

	}

	public static SponsorDto generateSponsorDto() {

		final Timestamp timestamp = Timestamp.from(Instant.now());
		return new SponsorDto(UUID.randomUUID(), Instant.now(), Instant.now(), UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), RANDOM.nextInt(500), generateContactInformation());

	}

	public static Sponsor generateSponsor() {

		final Instant now = Instant.now();
		return new Sponsor(UUID.randomUUID(), now, now, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
				RANDOM.nextInt(500), generateContactInformation());

	}

	public static ContactInformation generateContactInformation() {

		return new ContactInformation(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
				UUID.randomUUID().toString());

	}
}
