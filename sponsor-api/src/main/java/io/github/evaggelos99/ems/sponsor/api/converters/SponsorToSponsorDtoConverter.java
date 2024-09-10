package io.github.evaggelos99.ems.sponsor.api.converters;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.github.evaggelos99.ems.sponsor.api.Sponsor;
import io.github.evaggelos99.ems.sponsor.api.SponsorDto;

@Component
public class SponsorToSponsorDtoConverter implements Function<Sponsor, SponsorDto> {

	@Override
	public SponsorDto apply(final Sponsor sponsor) {

		return new SponsorDto(sponsor.getUuid(), sponsor.getCreatedAt(), (sponsor.getLastUpdated()), sponsor.getName(),
				sponsor.getWebsite(), sponsor.getFinancialContribution(), sponsor.getContactInformation());

	}

}
