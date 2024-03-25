package org.com.ems.controller.api;

import org.com.ems.api.dao.Event;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/event")
public interface IEventController {

	/**
	 * Method that creates an Event DAO object and saves it in the DB
	 * 
	 * @param event
	 */
	@PostMapping
	Event postEvent(@Valid @RequestBody Event event);

	/**
	 * Method that gets an Event DAO object from the DB
	 * 
	 * @param eventId the UUID that will be used to search for the Event
	 */
	@GetMapping("/{eventId}")
	Event getEvent(@PathVariable String eventId);

	/**
	 * Method that updates the Event with that eventId If the id does not match any
	 * Event stored in the DB it will throw {@link ObjectNotFoundException}
	 * 
	 * @param eventId the UUID of the Event object
	 * @param event   the edited Event object
	 */
	@PutMapping("/{eventId}")
	Event updateEvent(@PathVariable String eventId, @Valid @RequestBody Event event);

	/**
	 * Method that deletes the event with the specific eventId
	 * 
	 * @param eventId the UUID of the Event object
	 */
	@DeleteMapping(path = "/{eventId}")
	void deleteEvent(@PathVariable String eventId);

}
