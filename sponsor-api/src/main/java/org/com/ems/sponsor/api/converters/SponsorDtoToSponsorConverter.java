package org.com.ems.sponsor.api.converters;

import java.util.function.Function;

import org.com.ems.sponsor.api.Sponsor;
import org.com.ems.sponsor.api.SponsorDto;
import org.springframework.stereotype.Component;

@Component
public class SponsorDtoToSponsorConverter implements Function<SponsorDto, Sponsor> {

    @Override
    public Sponsor apply(final SponsorDto sponsorDto) {

	return new Sponsor(sponsorDto.uuid(), sponsorDto.createdAt().toInstant(), sponsorDto.lastUpdated().toInstant(),
		sponsorDto.denomination(), sponsorDto.website(), sponsorDto.financialContribution(),
		sponsorDto.contactInformation());

    }

}
