package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.UUID;

import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.controller.api.ITicketController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.ITicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Ticket> postTicket(final Ticket ticket) {

		try {
			return ResponseEntity.created(new URI("/ticket/")).body(this.ticketRepository.save(ticket));
		} catch (final URISyntaxException e) {

			return new ResponseEntity<>(this.ticketRepository.save(ticket), HttpStatus.CREATED);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Ticket> getTicket(final UUID ticketId) {

		final var optionalTicket = this.ticketRepository.findById(ticketId);

		return ResponseEntity.of(optionalTicket);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Ticket> putTicket(final UUID ticketId, final Ticket ticket) {

		if (this.ticketRepository.existsById(ticketId)) {

			try {
				return ResponseEntity.created(new URI("/ticket/" + ticketId)).body(this.ticketRepository.save(ticket));
			} catch (final URISyntaxException e) {

				return new ResponseEntity<>(this.ticketRepository.save(ticket), HttpStatus.CREATED);
			}
		}

		throw new ObjectNotFoundException(ticketId, Ticket.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<?> deleteTicket(final UUID ticketId) {

		if (!this.ticketRepository.existsById(ticketId)) {

			throw new ObjectNotFoundException(ticketId, Ticket.class);
		}

		this.ticketRepository.deleteById(ticketId);

		return ResponseEntity.noContent().build();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Collection<Ticket>> getTickets() {

		return ResponseEntity.ok().body(this.ticketRepository.findAll());
	}

}
