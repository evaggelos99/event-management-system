package org.com.ems.controller.api;

import org.com.ems.api.dao.Organizer;
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

public interface IOrganizerController {

	/**
	 * Method that creates an Organizer DAO object and saves it in the DB
	 * 
	 * @param organizer
	 */
	@PostMapping
	Organizer postOrganizer(@Valid @RequestBody Organizer organizer);

	/**
	 * Method that gets an Organizer DAO object from the DB
	 * 
	 * @param organizerId the UUID that will be used to search for the Organizer
	 */
	@GetMapping("/{organizerId}")
	Organizer getOrganizer(@PathVariable String organizerId);

	/**
	 * Method that updates the Organizer with that organizerId If the id does not
	 * match any Attendee stored in the DB it will throw
	 * {@link ObjectNotFoundException}
	 * 
	 * @param organizerId the UUID of the Organizer object
	 * @param organizer   the edited Organizer object
	 */
	@PutMapping("/{organizerId}")
	Organizer updateOrganizer(@PathVariable String organizerId, @Valid @RequestBody Organizer organizer);

	/**
	 * Method that deletes the Organizer with the specific organizerId
	 * 
	 * @param organizerId the UUID of the Organizer object
	 */
	@DeleteMapping(path = "/{organizerId}")
	void deleteOrganizer(@PathVariable String organizerId);

}
