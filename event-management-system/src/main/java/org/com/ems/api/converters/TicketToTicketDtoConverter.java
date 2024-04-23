package org.com.ems.api.converters;

import java.util.function.Function;

import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.dto.TicketDto;
import org.springframework.stereotype.Component;

@Component
public class TicketToTicketDtoConverter implements Function<Ticket, TicketDto> {

	@Override
	public TicketDto apply(final Ticket ticket) {

		return new TicketDto(ticket.getUuid(), ticket.getLastUpdated(), ticket.getEventID(), ticket.getTicketType(),
				ticket.getPrice(), ticket.getTransferable(), ticket.getSeatInfo());
	}

}
