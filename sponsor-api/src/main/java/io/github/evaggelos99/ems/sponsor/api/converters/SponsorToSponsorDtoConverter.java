package io.github.evaggelos99.ems.sponsor.api.converters;

import io.github.evaggelos99.ems.sponsor.api.Sponsor;
import io.github.evaggelos99.ems.sponsor.api.SponsorDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SponsorToSponsorDtoConverter implements Function<Sponsor, SponsorDto> {

    @Override
    public SponsorDto apply(final Sponsor sponsor) {

        return new SponsorDto(sponsor.getUuid(), sponsor.getCreatedAt(), (sponsor.getLastUpdated()), sponsor.getName(),
                sponsor.getWebsite(), sponsor.getFinancialContribution(), sponsor.getContactInformation());

    }

}
