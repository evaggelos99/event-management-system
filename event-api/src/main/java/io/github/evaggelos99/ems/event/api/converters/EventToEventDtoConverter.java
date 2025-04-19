package io.github.evaggelos99.ems.event.api.converters;

import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EventToEventDtoConverter implements Function<Event, EventDto> {

    @Override
    public EventDto apply(final Event event) {

        return EventDto.builder()
                .uuid(event.getUuid())
                .createdAt(event.getCreatedAt())
                .lastUpdated(event.getLastUpdated())
                .name(event.getName())
                .place(event.getPlace())
                .eventType(event.getEventType())
                .attendeesIds(event.getAttendeesIDs())
                .organizerId(event.getOrganizerID())
                .limitOfPeople(event.getLimitOfPeople())
                .sponsorsIds(event.getSponsorsIds())
                .streamable(event.isStreamable())
                .startTimeOfEvent(event.getStartTime())
                .duration(event.getDuration())
                .build();
    }

}
