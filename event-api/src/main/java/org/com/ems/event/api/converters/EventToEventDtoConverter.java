package org.com.ems.event.api.converters;

import java.util.function.Function;

import org.com.ems.event.api.Event;
import org.com.ems.event.api.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventToEventDtoConverter implements Function<Event, EventDto> {

	@Override
	public EventDto apply(final Event event) {

		return new EventDto(event.getUuid(), event.getCreatedAt(), event.getLastUpdated(), event.getName(),
				event.getPlace(), event.getEventType(), event.getAttendeesIDs(), event.getOrganizerID(),
				event.getLimitOfPeople(), event.getSponsorsIds(), event.getStartTime(), event.getDuration());

	}

}
