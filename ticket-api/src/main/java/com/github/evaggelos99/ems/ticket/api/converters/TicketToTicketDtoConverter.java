package com.github.evaggelos99.ems.ticket.api.converters;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.github.evaggelos99.ems.ticket.api.Ticket;
import com.github.evaggelos99.ems.ticket.api.TicketDto;

@Component
public class TicketToTicketDtoConverter implements Function<Ticket, TicketDto> {

	@Override
	public TicketDto apply(final Ticket ticket) {

		return new TicketDto(ticket.getUuid(), ticket.getCreatedAt(), ticket.getLastUpdated(), ticket.getEventID(),
				ticket.getTicketType(), ticket.getPrice(), ticket.getTransferable(), ticket.getSeatingInformation());

	}

}
