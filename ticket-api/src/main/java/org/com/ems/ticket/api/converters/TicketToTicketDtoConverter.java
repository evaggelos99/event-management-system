package org.com.ems.ticket.api.converters;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.function.Function;

import org.com.ems.ticket.api.Ticket;
import org.com.ems.ticket.api.TicketDto;
import org.springframework.stereotype.Component;

@Component
public class TicketToTicketDtoConverter implements Function<Ticket, TicketDto> {

    @Override
    public TicketDto apply(final Ticket ticket) {

	return new TicketDto(ticket.getUuid(), this.convertToTimeStamp(ticket.getCreatedAt()),
		this.convertToTimeStamp(ticket.getLastUpdated()), ticket.getEventID(), ticket.getTicketType(),
		ticket.getPrice(), ticket.getTransferable(), ticket.getSeatInformation());

    }

    private Timestamp convertToTimeStamp(final Instant lastUpdated) {

	return Timestamp.from(lastUpdated);

    }

}
