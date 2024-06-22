package org.com.ems.organizer.api.converters;

import java.util.function.Function;

import org.com.ems.organizer.api.Organizer;
import org.com.ems.organizer.api.OrganizerDto;
import org.springframework.stereotype.Component;

@Component
public class OrganizerDtoToOrganizerConverter implements Function<OrganizerDto, Organizer> {

    @Override
    public Organizer apply(final OrganizerDto organizerDto) {

	return new Organizer(organizerDto.uuid(), organizerDto.createdAt().toInstant(),
		organizerDto.lastUpdated().toInstant(), organizerDto.name(), organizerDto.website(),
		organizerDto.information(), organizerDto.eventTypes(), organizerDto.contactInformation());

    }

}
