package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.UUID;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.controller.api.IEventController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for CRUD operation for the DAO object {@link Event}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping("/event")
public class EventController implements IEventController {

	private final IEventRepository eventRepository;

	/**
	 * C-or responsible for CRUD operations for the Object {@link Event}
	 *
	 * @param eventRepository
	 */
	public EventController(@Autowired final IEventRepository eventRepository) {
		this.eventRepository = requireNonNull(eventRepository);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Event> getEvent(final UUID eventId) {

		final var optionalEvent = this.eventRepository.findById(eventId);

		return ResponseEntity.of(optionalEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Event> putEvent(final UUID eventId, final Event event) {

		if (this.eventRepository.existsById(eventId)) {

			try {
				return ResponseEntity.created(new URI("/event/" + eventId)).body(this.eventRepository.save(event));
			} catch (final URISyntaxException e) {

				return new ResponseEntity<>(this.eventRepository.save(event), HttpStatus.CREATED);
			}
		}

		throw new ObjectNotFoundException(eventId, Event.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<?> deleteEvent(final UUID eventId) {

		if (!this.eventRepository.existsById(eventId)) {

			throw new ObjectNotFoundException(eventId, Event.class);
		}

		this.eventRepository.deleteById(eventId);

		return ResponseEntity.noContent().build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Event> postEvent(final Event event) {

		try {
			return ResponseEntity.created(new URI("/event/")).body(this.eventRepository.save(event));
		} catch (final URISyntaxException e) {

			return new ResponseEntity<>(this.eventRepository.save(event), HttpStatus.CREATED);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Collection<Event>> getEvents() {

		return ResponseEntity.ok().body(this.eventRepository.findAll());
	}

}
