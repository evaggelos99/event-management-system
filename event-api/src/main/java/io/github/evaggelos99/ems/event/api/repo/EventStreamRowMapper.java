package io.github.evaggelos99.ems.event.api.repo;

import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventStreamEntity;
import io.r2dbc.postgresql.codec.Interval;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
public class EventStreamRowMapper implements BiFunction<Row, RowMetadata, EventStreamEntity> {

    @Override
    public EventStreamEntity apply(final Row rs, final RowMetadata rmd) {

        return new EventStreamEntity(rs.get("id", UUID.class), rs.get("created_at", OffsetDateTime.class).toInstant(),
                rs.get("last_updated", OffsetDateTime.class).toInstant(),
                rs.get("stream_type", String.class),
                rs.get("inception_time", OffsetDateTime.class).toInstant(),
                rs.get("message_type", String.class),
                rs.get("content", String.class),
                rs.get("language", String.class),
                rs.get("is_important", Boolean.class),
                rs.get("metadata", Json.class));

    }

}
