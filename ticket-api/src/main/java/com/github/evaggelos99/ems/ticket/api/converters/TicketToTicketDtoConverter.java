package com.github.evaggelos99.ems.ticket.api.converters;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.github.evaggelos99.ems.ticket.api.Ticket;
import com.github.evaggelos99.ems.ticket.api.TicketDto;

@Component
public class TicketToTicketDtoConverter implements Function<Ticket, TicketDto> {

    @Override
    public TicketDto apply(final Ticket ticket) {

	return new TicketDto(ticket.getUuid(), this.convertToTimeStamp(ticket.getCreatedAt()),
		this.convertToTimeStamp(ticket.getLastUpdated()), ticket.getEventID(), ticket.getTicketType(),
		ticket.getPrice(), ticket.getTransferable(), ticket.getSeatingInformation());

    }

    private Timestamp convertToTimeStamp(final Instant lastUpdated) {

	return Timestamp.from(lastUpdated);

    }

}
