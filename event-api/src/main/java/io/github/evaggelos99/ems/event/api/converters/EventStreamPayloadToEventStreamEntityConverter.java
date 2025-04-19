package io.github.evaggelos99.ems.event.api.converters;

import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.api.EventStreamEntity;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EventStreamPayloadToEventStreamEntityConverter {

    public EventStreamEntity convert(final EventStreamPayload eventStreamPayload, Instant createdAt) {

        return new EventStreamEntity(eventStreamPayload.getUuid(), createdAt, createdAt,
                eventStreamPayload.getStreamType(),
                eventStreamPayload.getTime(),
                eventStreamPayload.getMessageType(),
                eventStreamPayload.getContent(),
                eventStreamPayload.getLanguage(),
                eventStreamPayload.getImportant(),
                Json.of(eventStreamPayload.getMetadata()));
    }
}
