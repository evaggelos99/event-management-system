package org.com.ems.services.impl;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.dto.TicketDto;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.ITicketRepository;
import org.com.ems.db.impl.TicketRepository;
import org.com.ems.services.api.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
