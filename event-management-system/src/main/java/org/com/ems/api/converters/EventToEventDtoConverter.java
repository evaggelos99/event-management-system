package org.com.ems.api.converters;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventToEventDtoConverter implements Function<Event, EventDto> {

    @Override
    public EventDto apply(final Event event) {

	return new EventDto(event.getId(), this.convertToTimeStamp(event.getCreatedAt()),
		this.convertToTimeStamp(event.getLastUpdated()), event.getName(), event.getPlace(),
		event.getEventType(), event.getAttendeesIDs(), event.getOrganizerID(), event.getLimitOfPeople(),
		event.getSponsorsIds(), event.getStartTime(), event.getDuration());

    }

    private Timestamp convertToTimeStamp(final Instant lastUpdated) {

	return Timestamp.from(lastUpdated);

    }

}
