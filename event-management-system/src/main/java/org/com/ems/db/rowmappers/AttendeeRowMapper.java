package org.com.ems.db.rowmappers;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Attendee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

@Component
public class AttendeeRowMapper implements BiFunction<Row, RowMetadata, Attendee> {

    private final Function<UUID[], List<UUID>> arrayToListOfUuid;

    public AttendeeRowMapper(@Autowired @Qualifier("arrayToListOfUuid") final Function<UUID[],
	    List<UUID>> arrayToListOfUuid) {

	this.arrayToListOfUuid = arrayToListOfUuid;

    }

    @Override
    public Attendee apply(final Row row,
			  final RowMetadata u) {

	final List<UUID> ticketIds = this.arrayToListOfUuid.apply((UUID[]) row.get("ticket_ids"));

	return new Attendee(UUID.fromString(row.get("id", String.class)),
		row.get("created_at", OffsetDateTime.class).toInstant(),
		row.get("last_updated", OffsetDateTime.class).toInstant(), row.get("first_name", String.class),
		row.get("last_name", String.class), ticketIds);

    }

}
