package org.com.ems.api.converters;

import java.util.function.Function;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventDtoToEventConverter implements Function<EventDto, Event> {

	@Override
	public Event apply(final EventDto eventDto) {

		return Event.builder().name(eventDto.name()).place(eventDto.place()).eventType(eventDto.eventType())
				.attendeesIDs(eventDto.attendeesIDs()).organizerID(eventDto.organizerID())
				.limitOfPeople(eventDto.limitOfPeople()).sponsorID(eventDto.sponsorID())
				.startTimeOfEvent(eventDto.startTimeOfEvent()).durationOfEvent(eventDto.durationOfEvent()).build();
	}

}
