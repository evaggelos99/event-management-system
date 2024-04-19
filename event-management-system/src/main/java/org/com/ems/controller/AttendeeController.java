package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.UUID;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.controller.api.IAttendeeController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IAttendeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for CRUD operation for the DAO object {@link Attendee}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping("/attendee")
public class AttendeeController implements IAttendeeController {

	private final IAttendeeRepository attendeeRepository;

	public AttendeeController(@Autowired final IAttendeeRepository attendeeRepository) {

		this.attendeeRepository = requireNonNull(attendeeRepository);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws URISyntaxException
	 */
	@Override
	public ResponseEntity<Attendee> postAttendee(final Attendee attendee) {

		try {
			return ResponseEntity.created(new URI("/attendee/")).body(this.attendeeRepository.save(attendee));
		} catch (final URISyntaxException e) {

			return new ResponseEntity<>(this.attendeeRepository.save(attendee), HttpStatus.CREATED);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Attendee> getAttendee(final UUID attendeeId) {

		final var optionalAttendee = this.attendeeRepository.findById(attendeeId);

		return ResponseEntity.of(optionalAttendee);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public ResponseEntity<Attendee> putAttendee(final UUID attendeeId, final Attendee attendee) {

		if (this.attendeeRepository.existsById(attendeeId)) {

			try {
				return ResponseEntity.created(new URI("/attendee/" + attendeeId))
						.body(this.attendeeRepository.save(attendee));
			} catch (final URISyntaxException e) {

				return new ResponseEntity<>(this.attendeeRepository.save(attendee), HttpStatus.CREATED);
			}
		}

		throw new ObjectNotFoundException(attendeeId, Attendee.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<?> deleteAttendee(final UUID attendeeId) {

		if (!this.attendeeRepository.existsById(attendeeId)) {

			throw new ObjectNotFoundException(attendeeId, Attendee.class);
		}

		this.attendeeRepository.deleteById(attendeeId);

		return ResponseEntity.noContent().build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Collection<Attendee>> getAttendees() {

		return ResponseEntity.ok().body(this.attendeeRepository.findAll());
	}

}
