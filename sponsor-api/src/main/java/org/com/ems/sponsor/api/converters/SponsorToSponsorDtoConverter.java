package org.com.ems.sponsor.api.converters;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.function.Function;

import org.com.ems.sponsor.api.Sponsor;
import org.com.ems.sponsor.api.SponsorDto;
import org.springframework.stereotype.Component;

@Component
public class SponsorToSponsorDtoConverter implements Function<Sponsor, SponsorDto> {

    @Override
    public SponsorDto apply(final Sponsor sponsor) {

	return new SponsorDto(sponsor.getUuid(), this.convertToTimeStamp(sponsor.getCreatedAt()),
		this.convertToTimeStamp(sponsor.getLastUpdated()), sponsor.getDenomination(), sponsor.getWebsite(),
		sponsor.getFinancialContribution(), sponsor.getContactInformation());

    }

    private Timestamp convertToTimeStamp(final Instant lastUpdated) {

	return Timestamp.from(lastUpdated);

    }
}
