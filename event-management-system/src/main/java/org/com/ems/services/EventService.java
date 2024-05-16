package org.com.ems.services;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;
import org.com.ems.db.IEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService implements IService<Event, EventDto> {

    private final IEventRepository eventRepository;

    public EventService(@Autowired final IEventRepository eventRepository) {

	this.eventRepository = eventRepository;

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

}
