package io.github.evaggelos99.ems.ticket.api.converters;

import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TicketDtoToTicketConverter implements Function<TicketDto, Ticket> {

    @Override
    public Ticket apply(final TicketDto ticketDto) {


        return new Ticket(ticketDto.uuid(), ticketDto.createdAt(), ticketDto.lastUpdated(), ticketDto.eventID(),
                ticketDto.ticketType(), ticketDto.price(), ticketDto.transferable(), ticketDto.seatInformation());

    }

}
