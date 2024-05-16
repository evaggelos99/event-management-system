package org.com.ems.db.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.Sponsor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class SponsorRowMapper implements RowMapper<Sponsor> {

    @Override
    public Sponsor mapRow(final ResultSet rs,
			  final int rowNum)
	    throws SQLException {

	final ContactInformation contactInformation = rs.getString("email") != null ? // all of them must not be null
										      // but check just one
		new ContactInformation(rs.getString("email"), rs.getString("phone_number"),
			rs.getString("physical_address"))
		: null;

	return new Sponsor(UUID.fromString(rs.getString("uuid")), rs.getTimestamp("last_updated").toInstant(),
		rs.getString("name"), rs.getString("website"), rs.getInt("financial_contribution"), contactInformation);

    }

}
