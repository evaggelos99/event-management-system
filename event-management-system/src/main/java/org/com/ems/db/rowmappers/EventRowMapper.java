package org.com.ems.db.rowmappers;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.domainobjects.EventType;
import org.postgresql.util.PGInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class EventRowMapper implements RowMapper<Event> {

    private final Function<Array, List<UUID>> arrayToListOfUuid;

    public EventRowMapper(@Autowired @Qualifier("arrayToListOfUuid") final Function<Array,
	    List<UUID>> arrayToListOfUuid) {

	this.arrayToListOfUuid = arrayToListOfUuid;

    }

    @Override
    public Event mapRow(final ResultSet rs,
			final int rowNum)
	    throws SQLException {

	final List<UUID> attendees = this.arrayToListOfUuid.apply(rs.getArray("attendee_ids"));

	final PGInterval pgInterval = (PGInterval) rs.getObject("duration");

	final int hours = pgInterval.getHours();
	final int mins = pgInterval.getMinutes();
	final double seconds = pgInterval.getSeconds();
	final int ms = pgInterval.getMicroSeconds();

	final Duration duration = Duration.ofHours(hours).plusMinutes(mins).plusSeconds((long) seconds).plusMillis(ms);

	return new Event(UUID.fromString(rs.getString("uuid")), rs.getTimestamp("last_updated").toInstant(),
		rs.getString("name"), rs.getString("place"), EventType.valueOf(rs.getString("event_type")), attendees,
		UUID.fromString(rs.getString("organizer_id")), rs.getInt("limit_of_people"),
		UUID.fromString(rs.getString("sponsor_id")), rs.getTimestamp("start_time").toLocalDateTime(), duration);

    }

}
