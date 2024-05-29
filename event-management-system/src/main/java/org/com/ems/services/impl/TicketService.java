package org.com.ems.services.impl;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.dto.TicketDto;
import org.com.ems.db.ITicketRepository;
import org.com.ems.db.impl.TicketRepository;
import org.com.ems.services.api.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Ticket add(final TicketDto attendee) {

	return this.ticketRepository.save(attendee);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Ticket> get(final UUID id) {

	return this.ticketRepository.findById(id);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final UUID id) {

	this.ticketRepository.deleteById(id);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ticket edit(final UUID id,
		       final TicketDto attendee) {

	if (!this.ticketRepository.existsById(id))
	    throw new NoSuchElementException();

	return this.ticketRepository.edit(attendee);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Ticket> getAll() {

	return this.ticketRepository.findAll();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(final UUID attendeeId) {

	return this.ticketRepository.existsById(attendeeId);

    }

}
