package io.github.evaggelos99.ems.organizer.api.converters;

import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.github.evaggelos99.ems.organizer.api.OrganizerDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrganizerDtoToOrganizerConverter implements Function<OrganizerDto, Organizer> {

    @Override
    public Organizer apply(final OrganizerDto organizerDto) {

        return new Organizer(organizerDto.uuid(), organizerDto.createdAt(), organizerDto.lastUpdated(),
                organizerDto.name(), organizerDto.website(), organizerDto.information(), organizerDto.eventTypes(),
                organizerDto.contactInformation());

    }

}
