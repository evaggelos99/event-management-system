package org.com.ems.api.converters;

import java.util.function.Function;

import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.api.dto.SponsorDto;
import org.springframework.stereotype.Component;

@Component
public class SponsorDtoToSponsorConverter implements Function<SponsorDto, Sponsor> {

	@Override
	public Sponsor apply(final SponsorDto sponsorDto) {

		return Sponsor.builder().name(sponsorDto.name()).website(sponsorDto.website())
				.financialContribution(sponsorDto.financialContribution())
				.contactInformation(sponsorDto.contactInformation()).build();
	}

}
