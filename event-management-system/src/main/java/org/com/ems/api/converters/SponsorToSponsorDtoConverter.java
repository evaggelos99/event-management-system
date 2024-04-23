package org.com.ems.api.converters;

import java.util.function.Function;

import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.api.dto.SponsorDto;
import org.springframework.stereotype.Component;

@Component
public class SponsorToSponsorDtoConverter implements Function<Sponsor, SponsorDto> {

	@Override
	public SponsorDto apply(final Sponsor sponsor) {

		return new SponsorDto(sponsor.getUuid(), sponsor.getLastUpdated(), sponsor.getName(), sponsor.getWebsite(),
				sponsor.getFinancialContribution(), sponsor.getContactInformation());
	}
}
