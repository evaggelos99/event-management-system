package org.com.ems.attendee.api.repo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.com.ems.attendee.api.Attendee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

@Component
public class AttendeeRowMapper implements BiFunction<Row, RowMetadata, Attendee> {

	private final Function<UUID[], List<UUID>> arrayToListOfUuidConverter;

	public AttendeeRowMapper(
			@Autowired @Qualifier("arrayToListOfUuidConverter") final Function<UUID[], List<UUID>> arrayToListOfUuidConverter) {

		this.arrayToListOfUuidConverter = arrayToListOfUuidConverter;
	}

	@Override
	public Attendee apply(final Row row, final RowMetadata u) {

		final List<UUID> ticketIds = this.arrayToListOfUuidConverter.apply((UUID[]) row.get("ticket_ids"));
		return new Attendee(row.get("id", UUID.class), row.get("created_at", OffsetDateTime.class).toInstant(),
				row.get("last_updated", OffsetDateTime.class).toInstant(), row.get("first_name", String.class),
				row.get("last_name", String.class), ticketIds);
	}
}