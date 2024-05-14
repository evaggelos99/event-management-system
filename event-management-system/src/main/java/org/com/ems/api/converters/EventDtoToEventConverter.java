package org.com.ems.api.converters;

import java.util.function.Function;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventDtoToEventConverter implements Function<EventDto, Event> {

    @Override
    public Event apply(final EventDto eventDto) {

	return new Event(eventDto.uuid(), eventDto.lastUpdated().toInstant(), eventDto.name(), eventDto.place(),
		eventDto.eventType(), eventDto.attendeesIDs(), eventDto.organizerID(), eventDto.limitOfPeople(),
		eventDto.sponsorID(), eventDto.startTimeOfEvent(), eventDto.durationOfEvent());

    }

}
