package com.github.evaggelos99.ems.ticket.service.controller;

import static java.util.Objects.requireNonNull;

import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.evaggelos99.ems.common.api.service.IService;
import com.github.evaggelos99.ems.ticket.api.ITicketController;
import com.github.evaggelos99.ems.ticket.api.Ticket;
import com.github.evaggelos99.ems.ticket.api.TicketDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
	public TicketController(@Autowired final IService<Ticket, TicketDto> ticketService,
			@Autowired @Qualifier("ticketToTicketDtoConverter") final Function<Ticket, TicketDto> ticketToTicketDtoConverter) {

		this.ticketService = requireNonNull(ticketService);
		this.ticketToTicketDtoConverter = requireNonNull(ticketToTicketDtoConverter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<TicketDto> postTicket(final TicketDto ticketDto) {

		return ticketService.add(ticketDto).map(ticketToTicketDtoConverter::apply);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<TicketDto> getTicket(final UUID ticketId) {

		return ticketService.get(ticketId).map(ticketToTicketDtoConverter::apply);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<TicketDto> putTicket(final UUID ticketId, final TicketDto ticketDto) {

		return ticketService.edit(ticketId, ticketDto).map(ticketToTicketDtoConverter::apply);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<?> deleteTicket(final UUID ticketId) {

		return ticketService.delete(ticketId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<TicketDto> getTickets() {

		return ticketService.getAll().map(ticketToTicketDtoConverter::apply);
	}

}
