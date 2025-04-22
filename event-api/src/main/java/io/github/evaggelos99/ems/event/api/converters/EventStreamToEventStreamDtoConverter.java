package io.github.evaggelos99.ems.event.api.converters;

import io.github.evaggelos99.ems.event.api.EventStream;
import io.github.evaggelos99.ems.event.api.EventStreamDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EventStreamToEventStreamDtoConverter implements Function<EventStream, EventStreamDto> {

    @Override
    public EventStreamDto apply(final EventStream eventStream) {

        return EventStreamDto.builder()
                .uuid(eventStream.getUuid())
                .createdAt(eventStream.getCreatedAt())
                .updatedAt(eventStream.getLastUpdated())
                .streamType(eventStream.getStreamType())
                .time(eventStream.getInceptionTime())
                .messageType(eventStream.getMessageType())
                .content(eventStream.getContent())
                .language(eventStream.getLanguage())
                .important(eventStream.getImportant())
                .metadata(eventStream.getMetadata())
                .build();
    }
}
