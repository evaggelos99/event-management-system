package org.com.ems.services.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.api.dto.EventDto;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IEventRepository;
import org.com.ems.services.api.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EventService implements IEventService {

    private final IEventRepository eventRepository;
    final Function<Event, EventDto> eventToEventDtoConverter;

    public EventService(@Autowired final IEventRepository eventRepository,
			@Autowired @Qualifier("eventToEventDtoConverter") final Function<Event,
				EventDto> eventToEventDtoConverter) {

	this.eventRepository = eventRepository;
	this.eventToEventDtoConverter = eventToEventDtoConverter;

    }

    @Override
    public Event add(final EventDto event) {

	return this.eventRepository.save(event);

    }

    @Override
    public Optional<Event> get(final UUID uuid) {

	return this.eventRepository.findById(uuid);

    }

    @Override
    public void delete(final UUID uuid) {

	this.eventRepository.deleteById(uuid);

    }

    @Override
    public Event edit(final UUID eventId,
		      final EventDto event) {

	if (!this.eventRepository.existsById(eventId))
	    throw new NoSuchElementException();

	return this.eventRepository.edit(event);

    }

    @Override
    public Collection<Event> getAll() {

	return this.eventRepository.findAll();

    }

    @Override
    public boolean existsById(final UUID eventId) {

	return this.eventRepository.existsById(eventId);

    }

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
		event.getSponsorID(), event.getStartTime(), event.getDuration());

	final EventDto dto = this.eventToEventDtoConverter.apply(newEvent);

	final Event eventFromRepo = this.eventRepository.edit(dto);

	if (!eventFromRepo.getAttendeesIDs().containsAll(list)) {

	    return false;
	}

	return true;

    }

}
