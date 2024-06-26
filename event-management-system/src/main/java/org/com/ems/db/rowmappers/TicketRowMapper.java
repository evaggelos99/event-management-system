package org.com.ems.db.rowmappers;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.com.ems.api.domainobjects.SeatingInformation;
import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.domainobjects.TicketType;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

@Component
public class TicketRowMapper implements BiFunction<Row, RowMetadata, Ticket> {

    @Override
    public Ticket apply(final Row row,
			final RowMetadata rmd) {

	final SeatingInformation seatingInformation = new SeatingInformation(row.get("seat", String.class),
		row.get("section", String.class));

	return new Ticket(UUID.fromString(row.get("id", String.class)),
		row.get("created_at", OffsetDateTime.class).toInstant(),
		row.get("last_updated", OffsetDateTime.class).toInstant(),
		UUID.fromString(row.get("event_id", String.class)),
		TicketType.valueOf(row.get("ticket_type", String.class)), row.get("price", Integer.class),
		row.get("transferable", Boolean.class), seatingInformation);

    }

}
