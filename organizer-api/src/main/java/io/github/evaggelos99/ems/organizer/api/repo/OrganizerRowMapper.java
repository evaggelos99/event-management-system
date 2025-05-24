package io.github.evaggelos99.ems.organizer.api.repo;

import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
public class OrganizerRowMapper implements BiFunction<Row, RowMetadata, Organizer> {

    private final Function<EventType[], List<EventType>> arrayToListOfEventTypesConverter;

    public OrganizerRowMapper(
            @Qualifier("arrayToListOfEventTypesConverter") final Function<EventType[], List<EventType>> arrayToListOfEventTypes) {

        this.arrayToListOfEventTypesConverter = arrayToListOfEventTypes;
    }

    @Override
    public Organizer apply(final Row row, final RowMetadata rmd) {

        final List<EventType> eventsTypes = arrayToListOfEventTypesConverter
                .apply((EventType[]) row.get("event_types"));

        final ContactInformation contactInformation = new ContactInformation(row.get("email", String.class),
                row.get("phone_number", String.class), row.get("physical_address", String.class));

        return new Organizer(row.get("id", UUID.class), row.get("created_at", OffsetDateTime.class),
                row.get("last_updated", OffsetDateTime.class), row.get("name", String.class),
                row.get("website", String.class), row.get("information", String.class), eventsTypes,
                contactInformation);
    }

}
