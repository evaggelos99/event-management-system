package org.com.ems.db.rowmappers;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.domainobjects.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.r2dbc.postgresql.codec.Interval;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

@Component
public class EventRowMapper implements BiFunction<Row, RowMetadata, Event> {

    private final Function<UUID[], List<UUID>> arrayToListOfUuid;

    public EventRowMapper(@Autowired @Qualifier("arrayToListOfUuid") final Function<UUID[],
	    List<UUID>> arrayToListOfUuid) {

	this.arrayToListOfUuid = arrayToListOfUuid;

    }

    @Override
    public Event apply(final Row rs,
		       final RowMetadata rmd) {

	final List<UUID> attendees = this.arrayToListOfUuid.apply((UUID[]) rs.get("attendee_ids"));

	final List<UUID> sponsors = this.arrayToListOfUuid.apply((UUID[]) rs.get("sponsors_ids"));

	final Duration duration = rs.get("duration", Interval.class).getDuration();

	return new Event(UUID.fromString(rs.get("id", String.class)),
		rs.get("created_at", OffsetDateTime.class).toInstant(),
		rs.get("last_updated", OffsetDateTime.class).toInstant(), rs.get("denomination", String.class),
		rs.get("place", String.class), EventType.valueOf(rs.get("event_type", String.class)), attendees,
		UUID.fromString(rs.get("organizer_id", String.class)), rs.get("limit_of_people", Integer.class),
		sponsors, rs.get("start_time", OffsetDateTime.class).toLocalDateTime(), duration);

    }

}
