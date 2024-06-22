package org.com.ems.sponsor.api.repo;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.com.ems.common.api.domainobjects.ContactInformation;
import org.com.ems.sponsor.api.Sponsor;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

@Component
public class SponsorRowMapper implements BiFunction<Row, RowMetadata, Sponsor> {

    @Override
    public Sponsor apply(final Row row,
			 final RowMetadata rmd) {

	final ContactInformation contactInformation = new ContactInformation(row.get("email", String.class),
		row.get("phone_number", String.class), row.get("physical_address", String.class));

	return new Sponsor(UUID.fromString(row.get("id", String.class)),
		row.get("created_at", OffsetDateTime.class).toInstant(),
		row.get("last_updated", OffsetDateTime.class).toInstant(), row.get("name", String.class),
		row.get("website", String.class), row.get("financial_contribution", Integer.class), contactInformation);

    }

}
