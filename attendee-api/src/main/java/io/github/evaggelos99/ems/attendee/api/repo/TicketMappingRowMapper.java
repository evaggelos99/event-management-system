package io.github.evaggelos99.ems.attendee.api.repo;

import io.github.evaggelos99.ems.attendee.api.AttendeeTicketMapping;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.BiFunction;

@Component
public class TicketMappingRowMapper implements BiFunction<Row, RowMetadata, AttendeeTicketMapping> {

    @Override
    public AttendeeTicketMapping apply(final Row row, final RowMetadata rowMetadata) {
        return new AttendeeTicketMapping(row.get("attendee_id", UUID.class), row.get("ticket_id", UUID.class));
    }
}
