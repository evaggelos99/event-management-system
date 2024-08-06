package com.github.evaggelos99.ems.organizer.api.converters;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.github.evaggelos99.ems.organizer.api.Organizer;
import com.github.evaggelos99.ems.organizer.api.OrganizerDto;

@Component
public class OrganizerToOrganizerDtoConverter implements Function<Organizer, OrganizerDto> {

	@Override
	public OrganizerDto apply(final Organizer organizer) {

		return new OrganizerDto(organizer.getUuid(), organizer.getCreatedAt(), organizer.getLastUpdated(),
				organizer.getName(), organizer.getWebsite(), organizer.getInformation(), organizer.getEventTypes(),
				organizer.getContactInformation());
	}

}
