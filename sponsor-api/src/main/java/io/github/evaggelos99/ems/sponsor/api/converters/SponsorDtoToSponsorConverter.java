package io.github.evaggelos99.ems.sponsor.api.converters;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.github.evaggelos99.ems.sponsor.api.Sponsor;
import io.github.evaggelos99.ems.sponsor.api.SponsorDto;

@Component
public class SponsorDtoToSponsorConverter implements Function<SponsorDto, Sponsor> {

	@Override
	public Sponsor apply(final SponsorDto sponsorDto) {

		return new Sponsor(sponsorDto.uuid(), sponsorDto.createdAt(), sponsorDto.lastUpdated(), sponsorDto.name(),
				sponsorDto.website(), sponsorDto.financialContribution(), sponsorDto.contactInformation());

	}

}
