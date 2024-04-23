package org.com.ems.api.converters;

import java.util.function.Function;

import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.api.dto.OrganizerDto;
import org.springframework.stereotype.Component;

@Component
public class OrganizerDtoToOrganizerConverter implements Function<OrganizerDto, Organizer> {

	@Override
	public Organizer apply(final OrganizerDto organizerDto) {

		return new Organizer(organizerDto.uuid(), organizerDto.lastUpdated(), organizerDto.name(),
				organizerDto.website(), organizerDto.description(), organizerDto.eventTypes(),
				organizerDto.contactInformation());
	}

}
