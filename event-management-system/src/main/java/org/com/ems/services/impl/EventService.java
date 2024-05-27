package org.com.ems.services.impl;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.converters.EventToEventDtoConverter;
import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.api.dto.EventDto;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IEventRepository;
import org.com.ems.db.impl.EventRepository;
import org.com.ems.services.api.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EventService implements IEventService {

    private final IEventRepository eventRepository;
    final Function<Event, EventDto> eventToEventDtoConverter;

    /**
     * C-or
     *
     * @param eventRepository          {@link EventRepository} the repository that
     *                                 communicates with the database
     * @param eventToEventDtoConverter {@link EventToEventDtoConverter} converts
     *                                 from Event to EventDto
     */
    public EventService(@Autowired final IEventRepository eventRepository,
			@Autowired @Qualifier("eventToEventDtoConverter") final Function<Event,
				EventDto> eventToEventDtoConverter) {

	this.eventRepository = requireNonNull(eventRepository);
	this.eventToEventDtoConverter = requireNonNull(eventToEventDtoConverter);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Event add(final EventDto event) {

	return this.eventRepository.save(event);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Event> get(final UUID uuid) {

	return this.eventRepository.findById(uuid);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final UUID uuid) {

	this.eventRepository.deleteById(uuid);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Event edit(final UUID uuid,
		      final EventDto event) {

	if (!this.eventRepository.existsById(uuid))
	    throw new NoSuchElementException();

	return this.eventRepository.edit(event);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Event> getAll() {

	return this.eventRepository.findAll();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(final UUID eventId) {

	return this.eventRepository.existsById(eventId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAttendee(final UUID eventId,
			       final UUID attendeeId) {

	final Optional<Event> optionalEvent = this.eventRepository.findById(eventId);

	final Event event = optionalEvent.orElseThrow(() -> new ObjectNotFoundException(eventId, AttendeeDto.class));

	final List<UUID> ids = event.getAttendeesIDs();

	final LinkedList<UUID> list = new LinkedList<>(ids);
	list.add(attendeeId);

	final Event newEvent = new Event(eventId, event.getCreatedAt(), event.getLastUpdated(), event.getDenomination(),
		event.getPlace(), event.getEventType(), list, event.getOrganizerID(), event.getLimitOfPeople(),
		event.getSponsorsIds(), event.getStartTime(), event.getDuration());

	final EventDto dto = this.eventToEventDtoConverter.apply(newEvent);

	final Event eventFromRepo = this.eventRepository.edit(dto);

	return eventFromRepo.getAttendeesIDs().containsAll(list);

    }

}
