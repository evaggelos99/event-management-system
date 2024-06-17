package org.com.ems.event.api.converters;

import java.util.function.Function;

import org.com.ems.event.api.Event;
import org.com.ems.event.api.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventDtoToEventConverter implements Function<EventDto, Event> {

    @Override
    public Event apply(final EventDto eventDto) {

	return new Event(eventDto.uuid(), eventDto.createdAt().toInstant(), eventDto.lastUpdated().toInstant(),
		eventDto.denomination(), eventDto.place(), eventDto.eventType(), eventDto.attendeesIds(),
		eventDto.organizerId(), eventDto.limitOfPeople(), eventDto.sponsorsIds(), eventDto.startTimeOfEvent(),
		eventDto.duration());

    }

}
