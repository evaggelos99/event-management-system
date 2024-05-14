package org.com.ems.services;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;
import org.springframework.stereotype.Service;

@Service
public class EventService implements IService<Event, EventDto> {

    @Override
    public Event add(final EventDto object) {

	return null;

    }

    @Override
    public Optional<Event> get(final UUID uuid) {

	// TODO Auto-generated method stub
	return Optional.empty();

    }

    @Override
    public void delete(final UUID uuid) {

	// TODO Auto-generated method stub

    }

    @Override
    public Event edit(final UUID uuid,
		      final EventDto object) {

	// TODO Auto-generated method stub
	return null;

    }

    @Override
    public Collection<Event> getAll() {

	// TODO Auto-generated method stub
	return null;

    }

    @Override
    public boolean existsById(final UUID objectId) {

	// TODO Auto-generated method stub
	return false;

    }

//    private final IEventRepository eventRepository;
//
//    public EventService(@Autowired final IEventRepository eventRepository) {
//
//	this.eventRepository = eventRepository;
//
//    }
//
//    @Override
//    public Event add(final Event attendee) {
//
//	return this.eventRepository.save(attendee);
//
//    }
//
//    @Override
//    public Optional<Event> get(final UUID uuid) {
//
//	return this.eventRepository.findById(uuid);
//
//    }
//
//    @Override
//    public void delete(final UUID uuid) {
//
//	this.eventRepository.deleteById(uuid);
//
//    }
//
//    @Override
//    public Event edit(final UUID uuid,
//		      final Event attendee) {
//
//	if (!this.eventRepository.existsById(uuid))
//	    throw new NoSuchElementException();
//
//	return this.eventRepository.save(attendee);
//
//    }
//
//    @Override
//    public Collection<Event> getAll() {
//
//	return this.eventRepository.findAll();
//
//    }
//
//    @Override
//    public boolean existsById(final UUID attendeeId) {
//
//	return this.eventRepository.existsById(attendeeId);
//
//    }

}
