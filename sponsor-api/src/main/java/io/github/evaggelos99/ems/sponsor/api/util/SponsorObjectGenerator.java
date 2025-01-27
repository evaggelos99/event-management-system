package io.github.evaggelos99.ems.sponsor.api.util;

import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.sponsor.api.Sponsor;
import io.github.evaggelos99.ems.sponsor.api.SponsorDto;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public final class SponsorObjectGenerator {

    private static final Random RANDOM = new Random();

    private SponsorObjectGenerator() {

    }

    public static SponsorDto generateSponsorDto(final UUID sponsorId) {

        return SponsorDto.builder()
                .uuid(sponsorId != null ? sponsorId : UUID.randomUUID())
                .createdAt(Instant.now())
                .lastUpdated(Instant.now())
                .name(UUID.randomUUID().toString())
                .website(UUID.randomUUID().toString())
                .financialContribution(RANDOM.nextInt(500))
                .contactInformation(generateContactInformation())
                .build();
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

    public static SponsorDto generateSponsorDtoWithoutTimestamps() {

        return SponsorDto.builder()
                .uuid(UUID.randomUUID())
                .name(UUID.randomUUID().toString())
                .website(UUID.randomUUID().toString())
                .financialContribution(RANDOM.nextInt(500))
                .contactInformation(generateContactInformation())
                .build();
    }
}
