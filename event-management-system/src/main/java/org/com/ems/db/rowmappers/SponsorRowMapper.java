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

	final ContactInformation contactInformation = new ContactInformation(rs.getString("email"),
		rs.getString("phone_number"), rs.getString("physical_address"));

	return new Sponsor(UUID.fromString(rs.getString("id")), rs.getTimestamp("last_updated").toInstant(),
		rs.getString("denomination"), rs.getString("website"), rs.getInt("financial_contribution"),
		contactInformation);

    }

}
