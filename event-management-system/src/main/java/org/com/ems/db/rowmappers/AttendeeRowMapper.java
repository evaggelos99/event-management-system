package org.com.ems.db.rowmappers;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Attendee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AttendeeRowMapper implements RowMapper<Attendee> {

    private final Function<Array, List<UUID>> arrayToListOfUuid;

    public AttendeeRowMapper(@Autowired @Qualifier("arrayToListOfUuid") final Function<Array,
	    List<UUID>> arrayToListOfUuid) {

	this.arrayToListOfUuid = arrayToListOfUuid;

    }

    @Override
    public Attendee mapRow(final ResultSet rs,
			   final int rowNum)
	    throws SQLException {

	final List<UUID> ticket_ids = this.arrayToListOfUuid.apply(rs.getArray("ticket_ids"));

	return new Attendee(UUID.fromString(rs.getString("id")), rs.getTimestamp("created_at").toInstant(),
		rs.getTimestamp("last_updated").toInstant(), rs.getString("first_name"), rs.getString("last_name"),
		ticket_ids);

    }

}
