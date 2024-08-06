package com.github.evaggelos99.ems.ticket.api.converters;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.github.evaggelos99.ems.ticket.api.Ticket;
import com.github.evaggelos99.ems.ticket.api.TicketDto;

@Component
public class TicketDtoToTicketConverter implements Function<TicketDto, Ticket> {

    @Override
    public Ticket apply(final TicketDto ticketDto) {

	return new Ticket(ticketDto.uuid(), ticketDto.createdAt().toInstant(), ticketDto.lastUpdated().toInstant(),
		ticketDto.eventID(), ticketDto.ticketType(), ticketDto.price(), ticketDto.transferable(),
		ticketDto.seatInformation());

    }

}
