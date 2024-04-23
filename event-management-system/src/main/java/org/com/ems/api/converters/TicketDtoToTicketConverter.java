package org.com.ems.api.converters;

import java.util.function.Function;

import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.dto.TicketDto;
import org.springframework.stereotype.Component;

@Component
public class TicketDtoToTicketConverter implements Function<TicketDto, Ticket> {

	@Override
	public Ticket apply(final TicketDto ticketDto) {

		return Ticket.builder().eventID(ticketDto.eventID()).ticketType(ticketDto.ticketType()).price(ticketDto.price())
				.transferable(ticketDto.transferable()).seatInfo(ticketDto.seatInfo()).build();
	}

}
