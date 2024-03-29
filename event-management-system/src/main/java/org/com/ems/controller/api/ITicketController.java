package org.com.ems.controller.api;

import java.util.Collection;

import org.com.ems.api.dao.Ticket;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

public interface ITicketController {

	/**
	 * Method that creates an Ticket DAO object and saves it in the DB
	 *
	 * @param ticket
	 */
	@PostMapping("/ticket")
	Ticket postTicket(@Valid @RequestBody Ticket ticket);

	/**
	 * Method that gets an Ticket DAO object from the DB
	 *
	 * @param ticketId the UUID that will be used to search for the Attendee
	 */
	@GetMapping("/ticket/{ticketId}")
	Ticket getTicket(@PathVariable String ticketId);

	/**
	 * Method that returns all Ticket objects from the DB
	 *
	 */
	@GetMapping()
	Collection<Ticket> getTickets();

	/**
	 * Method that updates the Ticket with that AttendeeId If the id does not match
	 * any Attendee stored in the DB it will throw {@link ObjectNotFoundException}
	 *
	 * @param ticketId the UUID of the Attendee object
	 * @param ticket   the edited Ticket object
	 */
	@PutMapping("/ticket/{ticketId}")
	Ticket putTicket(@PathVariable String ticketId, @Valid @RequestBody Ticket ticket);

	/**
	 * Method that deletes the Ticket with the specific AttendeeId
	 *
	 * @param ticketId the UUID of the Ticket object
	 */
	@DeleteMapping(path = "/ticket/{ticketId}")
	void deleteTicket(@PathVariable String ticketId);

}
