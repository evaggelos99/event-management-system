package io.github.evaggelos99.ems.event.api.repo;

import io.github.evaggelos99.ems.event.api.EventAttendeeMapping;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.BiFunction;

@Component
public class AttendeeMappingRowMapper implements BiFunction<Row, RowMetadata, EventAttendeeMapping> {

    @Override
    public EventAttendeeMapping apply(final Row row, final RowMetadata rowMetadata) {

        return new EventAttendeeMapping(row.get("event_id", UUID.class), row.get("attendee_id", UUID.class));
    }
}
