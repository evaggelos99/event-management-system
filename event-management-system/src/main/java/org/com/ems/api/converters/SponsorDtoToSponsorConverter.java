package org.com.ems.api.converters;

import java.util.function.Function;

import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.api.dto.SponsorDto;
import org.springframework.stereotype.Component;

@Component
public class SponsorDtoToSponsorConverter implements Function<SponsorDto, Sponsor> {

    @Override
    public Sponsor apply(final SponsorDto sponsorDto) {

	return new Sponsor(sponsorDto.uuid(), sponsorDto.lastUpdated(), sponsorDto.name(), sponsorDto.website(),
		sponsorDto.financialContribution(), sponsorDto.contactInformation());

    }

}
