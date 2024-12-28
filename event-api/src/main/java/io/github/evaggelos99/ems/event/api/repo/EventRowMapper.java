package io.github.evaggelos99.ems.event.api.repo;

import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.event.api.Event;
import io.r2dbc.postgresql.codec.Interval;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
public class EventRowMapper implements BiFunction<Row, RowMetadata, Event> {

    private final Function<UUID[], List<UUID>> arrayToListOfUuidConverter;

    public EventRowMapper(
            @Autowired @Qualifier("arrayToListOfUuidConverter") final Function<UUID[], List<UUID>> arrayToListOfUuid) {

        this.arrayToListOfUuidConverter = arrayToListOfUuid;
    }

    @Override
    public Event apply(final Row rs, final RowMetadata rmd) {

        final List<UUID> attendees = arrayToListOfUuidConverter.apply((UUID[]) rs.get("attendee_ids"));

        final List<UUID> sponsors = arrayToListOfUuidConverter.apply((UUID[]) rs.get("sponsors_ids"));

        final Duration duration = rs.get("duration", Interval.class).getDuration();

        return new Event(rs.get("id", UUID.class), rs.get("created_at", OffsetDateTime.class).toInstant(),
                rs.get("last_updated", OffsetDateTime.class).toInstant(), rs.get("name", String.class),
                rs.get("place", String.class), EventType.valueOf(rs.get("event_type", String.class)), attendees,
                rs.get("organizer_id", UUID.class), rs.get("limit_of_people", Integer.class), sponsors,
                rs.get("start_time", OffsetDateTime.class).toLocalDateTime(), duration);

    }

}
