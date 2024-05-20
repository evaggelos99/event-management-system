package org.com.ems.db.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.com.ems.api.domainobjects.SeatingInformation;
import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.domainobjects.TicketType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TicketRowMapper implements RowMapper<Ticket> {

    @Override
    public Ticket mapRow(final ResultSet rs,
			 final int rowNum)
	    throws SQLException {

	final SeatingInformation seatingInformation = new SeatingInformation(rs.getString("seat"),
		rs.getString("section"));

	return new Ticket(UUID.fromString(rs.getString("id")), rs.getTimestamp("last_updated").toInstant(),
		UUID.fromString(rs.getString("event_id")), TicketType.valueOf(rs.getString("ticket_type")),
		rs.getInt("price"), rs.getBoolean("transferable"), seatingInformation);

    }

}
