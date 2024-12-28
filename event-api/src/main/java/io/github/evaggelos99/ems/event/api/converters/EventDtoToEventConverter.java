package io.github.evaggelos99.ems.event.api.converters;

import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EventDtoToEventConverter implements Function<EventDto, Event> {

    @Override
    public Event apply(final EventDto eventDto) {

        return new Event(eventDto.uuid(), eventDto.createdAt(), eventDto.lastUpdated(), eventDto.name(),
                eventDto.place(), eventDto.eventType(), eventDto.attendeesIds(), eventDto.organizerId(),
                eventDto.limitOfPeople(), eventDto.sponsorsIds(), eventDto.startTimeOfEvent(), eventDto.duration());

    }

}
