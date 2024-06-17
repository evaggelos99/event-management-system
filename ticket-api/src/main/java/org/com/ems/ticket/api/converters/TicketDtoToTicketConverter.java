package org.com.ems.ticket.api.converters;

import java.util.function.Function;

import org.com.ems.ticket.api.Ticket;
import org.com.ems.ticket.api.TicketDto;
import org.springframework.stereotype.Component;

@Component
public class TicketDtoToTicketConverter implements Function<TicketDto, Ticket> {

    @Override
    public Ticket apply(final TicketDto ticketDto) {

	return new Ticket(ticketDto.uuid(), ticketDto.createdAt().toInstant(), ticketDto.lastUpdated().toInstant(),
		ticketDto.eventID(), ticketDto.ticketType(), ticketDto.price(), ticketDto.transferable(),
		ticketDto.seatInformation());

    }

}
