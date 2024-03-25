package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.com.ems.api.dao.Ticket;
import org.com.ems.controller.api.ITicketController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.controller.utils.CommonControllerUtils;
import org.com.ems.db.api.ITicketRepository;
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

	public TicketController(@Autowired ITicketRepository ticketRepository) {

		this.ticketRepository = requireNonNull(ticketRepository);
	}

	@Override
	public Ticket postTicket(Ticket ticket) {

		return ticketRepository.save(ticket);
	}

	@Override
	public Ticket getTicket(String ticketId) {

		UUID uuid = CommonControllerUtils.stringToUUID(ticketId);
		return ticketRepository.findById(uuid)
				.orElseThrow(() -> new ObjectNotFoundException(uuid, Ticket.class));
	}

	@Override
	public Ticket updateTicket(String ticketId, Ticket ticket) {
		
		UUID uuid = CommonControllerUtils.stringToUUID(ticketId);
		if (ticketRepository.existsById(uuid)) {
			ticketRepository.deleteById(uuid);
			return ticketRepository.save(ticket);
		}

		throw new ObjectNotFoundException(uuid, Ticket.class);
	}

	@Override
	public void deleteTicket(String ticketId) {

		ticketRepository.deleteById(CommonControllerUtils.stringToUUID(ticketId));
	}

}
