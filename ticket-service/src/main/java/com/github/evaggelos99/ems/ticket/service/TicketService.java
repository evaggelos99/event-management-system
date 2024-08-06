package com.github.evaggelos99.ems.ticket.service;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import com.github.evaggelos99.ems.common.api.service.IService;
import com.github.evaggelos99.ems.ticket.api.Ticket;
import com.github.evaggelos99.ems.ticket.api.TicketDto;
import com.github.evaggelos99.ems.ticket.api.repo.ITicketRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TicketService implements IService<Ticket, TicketDto> {

    private final ITicketRepository ticketRepository;

    /**
     * C-or
     *
     * @param ticketRepository {@link TicketRepository} the repository that
     *                         communicates with the database
     */
    public TicketService(@Autowired final ITicketRepository ticketRepository) {

	this.ticketRepository = requireNonNull(ticketRepository);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Ticket> add(final TicketDto attendee) {

	return this.ticketRepository.save(attendee);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Ticket> get(final UUID uuid) {

	return this.ticketRepository.findById(uuid);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

	return this.ticketRepository.deleteById(uuid);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Ticket> edit(final UUID uuid,
			     final TicketDto ticket) {

	return !uuid.equals(ticket.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, TicketDto.class))
		: this.ticketRepository.edit(ticket);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Ticket> getAll() {

	return this.ticketRepository.findAll();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID attendeeId) {

	return this.ticketRepository.existsById(attendeeId);

    }

}
