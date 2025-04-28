package io.github.evaggelos99.ems.event.api.repo;

import io.github.evaggelos99.ems.event.api.EventStream;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

@Component
public class EventStreamRowMapper implements BiFunction<Row, RowMetadata, EventStream> {

    @Override
    public EventStream apply(final Row rs, final RowMetadata rmd) {

        return new EventStream(rs.get("uuid", UUID.class), rs.get("created_at", OffsetDateTime.class),
                rs.get("last_updated", OffsetDateTime.class),
                rs.get("stream_type", String.class),
                rs.get("inception_time", OffsetDateTime.class).toInstant(),
                rs.get("message_type", String.class),
                rs.get("content", String.class),
                rs.get("language", String.class),
                rs.get("is_important", Boolean.class),
                rs.get("metadata", Json.class));

    }

}
