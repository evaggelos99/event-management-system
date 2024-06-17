package org.com.ems.organizer.api.repo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.com.ems.common.api.domainobjects.ContactInformation;
import org.com.ems.common.api.domainobjects.EventType;
import org.com.ems.organizer.api.Organizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

@Component
public class OrganizerRowMapper implements BiFunction<Row, RowMetadata, Organizer> {

    private final Function<EventType[], List<EventType>> arrayToListOfEventTypes;

    public OrganizerRowMapper(@Autowired @Qualifier("arrayToListOfEventTypes") final Function<EventType[],
	    List<EventType>> arrayToListOfEventTypes) {

	this.arrayToListOfEventTypes = arrayToListOfEventTypes;

    }

    @Override
    public Organizer apply(final Row row,
			   final RowMetadata rmd) {

	final List<EventType> eventsTypes = this.arrayToListOfEventTypes.apply((EventType[]) row.get("event_types"));

	final ContactInformation contactInformation = new ContactInformation(row.get("email", String.class),
		row.get("phone_number", String.class), row.get("physical_address", String.class));

	return new Organizer(UUID.fromString(row.get("id", String.class)),
		row.get("created_at", OffsetDateTime.class).toInstant(),
		row.get("last_updated", OffsetDateTime.class).toInstant(), row.get("denomination", String.class),
		row.get("website", String.class), row.get("information", String.class), eventsTypes,
		contactInformation);

    }

}
