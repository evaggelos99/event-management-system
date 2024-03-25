package org.com.ems.controller.api;

import org.com.ems.api.dao.Attendee;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

public interface IAttendeeController {

	/**
	 * Method that creates an Attendee DAO object and saves it in the DB
	 * 
	 * @param attendee
	 */
	@PostMapping
	Attendee postAttendee(@Valid @RequestBody Attendee attendee);

	/**
	 * Method that gets an Attendee DAO object from the DB
	 * 
	 * @param attendeeId the UUID that will be used to search for the Attendee
	 */
	@GetMapping("/{attendeeId}")
	Attendee getAttendee(@PathVariable String attendeeId);

	/**
	 * Method that updates the Attendee with that AttendeeId If the id does not
	 * match any Attendee stored in the DB it will throw
	 * {@link ObjectNotFoundException}
	 * 
	 * @param attendeeId the UUID of the Attendee object
	 * @param attendee   the edited Attendee object
	 */
	@PutMapping("/{attendeeId}")
	Attendee updateAttendee(@PathVariable String attendeeId, @Valid @RequestBody Attendee attendee);

	/**
	 * Method that deletes the Attendee with the specific attendeeId
	 * 
	 * @param attendeeId the UUID of the Attendee object
	 */
	@DeleteMapping(path = "/{attendeeId}")
	void deleteAttendee(@PathVariable String attendeeId);

}
