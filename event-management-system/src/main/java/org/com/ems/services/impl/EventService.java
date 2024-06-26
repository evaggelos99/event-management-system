package org.com.ems.services.impl;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.converters.EventToEventDtoConverter;
import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IEventRepository;
import org.com.ems.db.impl.EventRepository;
import org.com.ems.services.api.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<Event> add(final EventDto event) {

	return this.eventRepository.save(event);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Event> get(final UUID uuid) {

	return this.eventRepository.findById(uuid);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

	return this.eventRepository.deleteById(uuid);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Event> edit(final UUID uuid,
			    final EventDto event) {

	return !uuid.equals(event.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, EventDto.class))
		: this.eventRepository.edit(event);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Event> getAll() {

	return this.eventRepository.findAll();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID eventId) {

	return this.eventRepository.existsById(eventId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> addAttendee(final UUID eventId,
				     final UUID attendeeId) {

	return this.eventRepository.findById(eventId)
		.map(event -> this.addAttendeeIdToExistingList(eventId, attendeeId, event))//
		.map(this.eventToEventDtoConverter::apply)//
		.flatMap(this.eventRepository::edit)//
		.map(x -> x.getAttendeesIDs().contains(attendeeId));

    }

    private Event addAttendeeIdToExistingList(final UUID eventId,
					      final UUID attendeeId,
					      final Event event) {

	final List<UUID> ids = event.getAttendeesIDs();
	final LinkedList<UUID> list = new LinkedList<>(ids);
	list.add(attendeeId);
	return new Event(eventId, event.getCreatedAt(), Instant.now(), event.getDenomination(), event.getPlace(),
		event.getEventType(), list, event.getOrganizerID(), event.getLimitOfPeople(), event.getSponsorsIds(),
		event.getStartTime(), event.getDuration());

    }

}
