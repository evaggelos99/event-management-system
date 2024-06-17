package org.com.ems.organizer.api.converters;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.function.Function;

import org.com.ems.organizer.api.Organizer;
import org.com.ems.organizer.api.OrganizerDto;
import org.springframework.stereotype.Component;

@Component
public class OrganizerToOrganizerDtoConverter implements Function<Organizer, OrganizerDto> {

    @Override
    public OrganizerDto apply(final Organizer organizer) {

	return new OrganizerDto(organizer.getUuid(), this.convertToTimeStamp(organizer.getCreatedAt()),
		this.convertToTimeStamp(organizer.getLastUpdated()), organizer.getDenomination(),
		organizer.getWebsite(), organizer.getInformation(), organizer.getEventTypes(),
		organizer.getContactInformation());

    }

    private Timestamp convertToTimeStamp(final Instant lastUpdated) {

	return Timestamp.from(lastUpdated);

    }
}
