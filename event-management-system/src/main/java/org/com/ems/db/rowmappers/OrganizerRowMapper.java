package org.com.ems.db.rowmappers;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.domainobjects.Organizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class OrganizerRowMapper implements RowMapper<Organizer> {

    private final Function<Array, List<EventType>> arrayToListOfEventTypes;

    public OrganizerRowMapper(@Autowired @Qualifier("arrayToListOfEventTypes") final Function<Array,
	    List<EventType>> arrayToListOfEventTypes) {

	this.arrayToListOfEventTypes = arrayToListOfEventTypes;

    }

    @Override
    public Organizer mapRow(final ResultSet rs,
			    final int rowNum)
	    throws SQLException {

	final List<EventType> listOfEventTypes = this.arrayToListOfEventTypes.apply(rs.getArray("event_types"));

	final ContactInformation contactInformation = new ContactInformation(rs.getString("email"),
		rs.getString("phone_number"), rs.getString("physical_address"));

	return new Organizer(UUID.fromString(rs.getString("id")), rs.getTimestamp("last_updated").toInstant(),
		rs.getString("denomination"), rs.getString("website"), rs.getString("information"), listOfEventTypes,
		contactInformation);

    }

}
