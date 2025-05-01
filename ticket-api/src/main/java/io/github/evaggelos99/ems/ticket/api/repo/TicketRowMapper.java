package io.github.evaggelos99.ems.ticket.api.repo;

import io.github.evaggelos99.ems.common.api.domainobjects.SeatingInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.TicketType;
import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

@Component
public class TicketRowMapper implements BiFunction<Row, RowMetadata, Ticket> {

    @Override
    public Ticket apply(final Row row, final RowMetadata rmd) {

        final SeatingInformation seatingInformation = new SeatingInformation(row.get("seat", String.class),
                row.get("section", String.class));

        return new Ticket(row.get("id", UUID.class), row.get("created_at", OffsetDateTime.class),
                row.get("last_updated", OffsetDateTime.class), row.get("event_id", UUID.class),
                TicketType.valueOf(row.get("ticket_type", String.class)), row.get("price", Integer.class),
                row.get("transferable", Boolean.class), seatingInformation, row.get("used", Boolean.class));
    }

}
