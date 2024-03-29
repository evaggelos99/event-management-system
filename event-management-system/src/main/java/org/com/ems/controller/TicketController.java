package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.UUID;

import org.com.ems.api.dao.Ticket;
import org.com.ems.controller.api.ITicketController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.controller.utils.CommonControllerUtils;
import org.com.ems.db.ITicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for CRUD operation for the DAO object {@link Ticket}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping("/ticket")
public class TicketController implements ITicketController {

	private final ITicketRepository ticketRepository;

	public TicketController(@Autowired final ITicketRepository ticketRepository) {

		this.ticketRepository = requireNonNull(ticketRepository);
	}

	@Override
	public Ticket postTicket(final Ticket ticket) {

		return this.ticketRepository.save(ticket);
	}

	@Override
	public Ticket getTicket(final String ticketId) {

		final UUID uuid = CommonControllerUtils.stringToUUID(ticketId);
		return this.ticketRepository.findById(uuid).orElseThrow(() -> new ObjectNotFoundException(uuid, Ticket.class));
	}

	@Override
	public Ticket putTicket(final String ticketId, final Ticket ticket) {

		final UUID uuid = CommonControllerUtils.stringToUUID(ticketId);
		if (this.ticketRepository.existsById(uuid)) {
			return this.ticketRepository.save(ticket);
		}

		throw new ObjectNotFoundException(uuid, Ticket.class);
	}

	@Override
	public void deleteTicket(final String ticketId) {

		this.ticketRepository.deleteById(CommonControllerUtils.stringToUUID(ticketId));
	}

	@Override
	public Collection<Ticket> getTickets() {

		return this.ticketRepository.findAll();
	}

}
