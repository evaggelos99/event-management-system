package org.com.ems.organizer.api.converters;

import java.util.function.Function;

import org.com.ems.organizer.api.Organizer;
import org.com.ems.organizer.api.OrganizerDto;
import org.springframework.stereotype.Component;

@Component
public class OrganizerToOrganizerDtoConverter implements Function<Organizer, OrganizerDto> {

	@Override
	public OrganizerDto apply(final Organizer organizer) {

		return new OrganizerDto(organizer.getUuid(), organizer.getCreatedAt(), organizer.getLastUpdated(),
				organizer.getName(), organizer.getWebsite(), organizer.getInformation(), organizer.getEventTypes(),
				organizer.getContactInformation());

	}

}
