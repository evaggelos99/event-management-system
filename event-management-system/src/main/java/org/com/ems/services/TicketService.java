package org.com.ems.services;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.dto.TicketDto;
import org.com.ems.db.ITicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService implements IService<Ticket, TicketDto> {

    private final ITicketRepository ticketRepository;

    public TicketService(@Autowired final ITicketRepository ticketRepository) {

	this.ticketRepository = ticketRepository;

    }

    @Override
    public Ticket add(final TicketDto attendee) {

	return this.ticketRepository.save(attendee);

    }

    @Override
    public Optional<Ticket> get(final UUID uuid) {

	return this.ticketRepository.findById(uuid);

    }

    @Override
    public void delete(final UUID uuid) {

	this.ticketRepository.deleteById(uuid);

    }

    @Override
    public Ticket edit(final UUID uuid,
		       final TicketDto attendee) {

	if (!this.ticketRepository.existsById(uuid))
	    throw new NoSuchElementException();

	return this.ticketRepository.edit(attendee);

    }

    @Override
    public Collection<Ticket> getAll() {

	return this.ticketRepository.findAll();

    }

    @Override
    public boolean existsById(final UUID attendeeId) {

	return this.ticketRepository.existsById(attendeeId);

    }

}
