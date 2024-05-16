package org.com.ems.api.converters;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.api.dto.SponsorDto;
import org.springframework.stereotype.Component;

@Component
public class SponsorToSponsorDtoConverter implements Function<Sponsor, SponsorDto> {

    @Override
    public SponsorDto apply(final Sponsor sponsor) {

	return new SponsorDto(sponsor.getUuid(), this.convertToTimeStamp(sponsor.getLastUpdated()), sponsor.getName(),
		sponsor.getWebsite(), sponsor.getFinancialContribution(), sponsor.getContactInformation());

    }

    private Timestamp convertToTimeStamp(final Instant lastUpdated) {

	return Timestamp.from(lastUpdated);

    }
}
