package org.com.ems.db.rowmappers;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.com.ems.api.domainobjects.Attendee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AttendeeRowMapper implements RowMapper<Attendee> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttendeeRowMapper.class);

    @Override
    public Attendee mapRow(final ResultSet rs,
			   final int rowNum)
	    throws SQLException {

	final List<UUID> ticket_ids = this.convertUuidArrayToList(rs.getArray("ticket_ids"));

	return new Attendee(UUID.fromString(rs.getString("uuid")), rs.getTimestamp("last_updated").toInstant(),
		rs.getString("first_name"), rs.getString("last_name"), ticket_ids);

    }

    private List<UUID> convertUuidArrayToList(final Array array) {

	final List<UUID> list = new LinkedList<>();

	try {

	    for (final UUID uuid : (UUID[]) array.getArray()) {

		list.add(uuid);

	    }
	} catch (final SQLException e) {

	    LOGGER.error("Exception occured", e);
	} catch (final ClassCastException cce) {

	    LOGGER.error("The array was not of type UUID", cce);
	}

	return list;

    }

}
