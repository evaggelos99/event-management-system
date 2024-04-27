package org.com.ems.api.converters;

import java.util.function.Function;

import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.api.dto.OrganizerDto;
import org.springframework.stereotype.Component;

@Component
public class OrganizerToOrganizerDtoConverter implements Function<Organizer, OrganizerDto> {

    @Override
    public OrganizerDto apply(final Organizer organizer) {

	return new OrganizerDto(organizer.getUuid(), organizer.getLastUpdated(), organizer.getName(),
		organizer.getWebsite(), organizer.getDescription(), organizer.getEventTypes(),
		organizer.getContactInformation());

    }

}
