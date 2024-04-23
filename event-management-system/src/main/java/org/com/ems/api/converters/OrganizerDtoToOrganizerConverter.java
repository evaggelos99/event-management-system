package org.com.ems.api.converters;

import java.util.function.Function;

import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.api.dto.OrganizerDto;
import org.springframework.stereotype.Component;

@Component
public class OrganizerDtoToOrganizerConverter implements Function<OrganizerDto, Organizer> {

	@Override
	public Organizer apply(final OrganizerDto organizerDto) {

		return Organizer.builder().name(organizerDto.name()).website(organizerDto.website())
				.description(organizerDto.description()).eventTypes(organizerDto.eventTypes())
				.contactInformation(organizerDto.contactInformation()).build();
	}

}
