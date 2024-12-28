package io.github.evaggelos99.ems.event.api.converters;

import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EventToEventDtoConverter implements Function<Event, EventDto> {

    @Override
    public EventDto apply(final Event event) {

        return new EventDto(event.getUuid(), event.getCreatedAt(), event.getLastUpdated(), event.getName(),
                event.getPlace(), event.getEventType(), event.getAttendeesIDs(), event.getOrganizerID(),
                event.getLimitOfPeople(), event.getSponsorsIds(), event.getStartTime(), event.getDuration());

    }

}
