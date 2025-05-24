package io.github.evaggelos99.ems.event.api.converters;

import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.api.EventStream;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class EventStreamPayloadToEventStreamEntityConverter {

    public EventStream convert(final EventStreamPayload eventStreamPayload, OffsetDateTime createdAt) {

        return new EventStream(eventStreamPayload.getUuid(), createdAt, createdAt,
                eventStreamPayload.getStreamType(),
                eventStreamPayload.getTime(),
                eventStreamPayload.getMessageType(),
                eventStreamPayload.getContent(),
                eventStreamPayload.getLanguage(),
                eventStreamPayload.getImportant(),
                Json.of(eventStreamPayload.getMetadata()));
    }
}
