package io.github.evaggelos99.ems.ticket.service.controller;

import io.github.evaggelos99.ems.common.api.service.IService;
import io.github.evaggelos99.ems.ticket.api.ITicketController;
import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Controller for CRUD operation for the object {@link Ticket}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping(TicketController.TICKET_PATH)
public class TicketController implements ITicketController {

    static final String TICKET_PATH = "/ticket";
    private final IService<Ticket, TicketDto> ticketService;
    private final Function<Ticket, TicketDto> ticketToTicketDtoConverter;

    /**
     * C-or
     *
     * @param ticketService              service responsible for CRUD operations
     * @param ticketToTicketDtoConverter ticket to DTO
     */
    public TicketController(final IService<Ticket, TicketDto> ticketService,
                            @Qualifier("ticketToTicketDtoConverter") final Function<Ticket, TicketDto> ticketToTicketDtoConverter) {

        this.ticketService = requireNonNull(ticketService);
        this.ticketToTicketDtoConverter = requireNonNull(ticketToTicketDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TicketDto> postTicket(final TicketDto ticketDto) {

        return ticketService.add(ticketDto).map(ticketToTicketDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TicketDto> getTicket(final UUID ticketId) {

        return ticketService.get(ticketId).map(ticketToTicketDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TicketDto> putTicket(final UUID ticketId, final TicketDto ticketDto) {

        return ticketService.edit(ticketId, ticketDto).map(ticketToTicketDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> deleteTicket(final UUID ticketId) {

        return ticketService.delete(ticketId);
    }

    @Override
    public Mono<Boolean> useTicket(final UUID ticketId) {

        // TODO FIXME add some kind of integrity check something that makes sense business wise idk

        return ticketService.get(ticketId)
                .map(ticketToTicketDtoConverter)
                .map(dto -> ticketService.edit(ticketId, TicketDto.from(dto).build()))
                .map(Objects::nonNull);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<TicketDto> getTickets() {

        return ticketService.getAll().map(ticketToTicketDtoConverter);
    }

    @Override
    public Mono<Boolean> ping() {

        return ticketService.ping().onErrorReturn(false);
    }

}
