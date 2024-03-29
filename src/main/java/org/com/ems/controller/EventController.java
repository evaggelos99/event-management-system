package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.UUID;

import org.com.ems.api.dao.Event;
import org.com.ems.controller.api.IEventController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.controller.utils.CommonControllerUtils;
import org.com.ems.db.IEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
	 * @param eventRepo
	 */
	public EventController(@Autowired final IEventRepository eventRepo) {
		this.eventRepository = requireNonNull(eventRepo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event getEvent(final String eventId) {

		final UUID uuid = CommonControllerUtils.stringToUUID(eventId);

		final var optionalEvent = this.eventRepository.findById(uuid);

		return optionalEvent.orElseThrow(() -> new ObjectNotFoundException(uuid, Event.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event putEvent(final String eventId, final Event event) {

		final UUID uuid = CommonControllerUtils.stringToUUID(eventId);

		if (this.eventRepository.existsById(uuid)) {
			return this.eventRepository.save(event);
		}

		throw new ObjectNotFoundException(uuid, Event.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteEvent(final String eventId) {

		this.eventRepository.deleteById(CommonControllerUtils.stringToUUID(eventId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event postEvent(final Event event) {

		return this.eventRepository.save(event);
	}

	@Override
	public Collection<Event> getEvents() {
		return this.eventRepository.findAll();
	}

}
