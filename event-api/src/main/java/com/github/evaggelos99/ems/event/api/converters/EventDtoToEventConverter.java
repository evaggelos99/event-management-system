package com.github.evaggelos99.ems.event.api.converters;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.github.evaggelos99.ems.event.api.Event;
import com.github.evaggelos99.ems.event.api.EventDto;

@Component
public class EventDtoToEventConverter implements Function<EventDto, Event> {

	@Override
	public Event apply(final EventDto eventDto) {

		return new Event(eventDto.uuid(), eventDto.createdAt(), eventDto.lastUpdated(), eventDto.name(),
				eventDto.place(), eventDto.eventType(), eventDto.attendeesIds(), eventDto.organizerId(),
				eventDto.limitOfPeople(), eventDto.sponsorsIds(), eventDto.startTimeOfEvent(), eventDto.duration());

	}

}
