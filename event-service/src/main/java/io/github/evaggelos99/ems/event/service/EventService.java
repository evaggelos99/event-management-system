package io.github.evaggelos99.ems.event.service;

import io.github.evaggelos99.ems.common.api.controller.exceptions.ConflictException;
import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.api.converters.EventToEventDtoConverter;
import io.github.evaggelos99.ems.event.api.repo.IEventRepository;
import io.github.evaggelos99.ems.event.api.service.IEventService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Service
public class EventService implements IEventService {

    private final IEventRepository eventRepository;
    private final Function<Event, EventDto> eventToEventDtoConverter;

    /**
     * C-or
     *
     * @param eventRepository          {@link EventRepository} the repository that
     *                                 communicates with the database
     * @param eventToEventDtoConverter {@link EventToEventDtoConverter} converts
     *                                 from Event to EventDto
     */
    public EventService(@Autowired final IEventRepository eventRepository, @Autowired @Qualifier("eventToEventDtoConverter") final Function<Event, EventDto> eventToEventDtoConverter) {

        this.eventRepository = requireNonNull(eventRepository);
        this.eventToEventDtoConverter = requireNonNull(eventToEventDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Event> add(final EventDto event) {

        return eventRepository.save(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Event> get(final UUID uuid) {

        return eventRepository.findById(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

        return eventRepository.deleteById(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Event> edit(final UUID uuid, final EventDto event) {

        return ObjectUtils.notEqual(uuid, event.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, EventDto.class)) : eventRepository.edit(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Event> getAll() {

        return eventRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID eventId) {

        return eventRepository.existsById(eventId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> addAttendee(final UUID eventId, final UUID attendeeId) {

        return eventRepository.findById(eventId)
                .map(event -> addAttendeeIdToExistingList(eventId, attendeeId, event))//
                .map(eventToEventDtoConverter)
                .flatMap(eventRepository::edit)
                .map(x -> x.getAttendeesIDs().contains(attendeeId))
                .onErrorReturn(false);
    }

    private Event addAttendeeIdToExistingList(final UUID eventId, final UUID attendeeId, final Event event) {

        final List<UUID> ids = event.getAttendeesIDs();

        if (ids.contains(attendeeId)) {

            throw new ConflictException(attendeeId, UUID.class);
        }

        final LinkedList<UUID> list = new LinkedList<>(ids);
        list.add(attendeeId);
        return new Event(eventId, event.getCreatedAt(), Instant.now(), event.getName(), event.getPlace(), event.getEventType(), list, event.getOrganizerID(), event.getLimitOfPeople(), event.getSponsorsIds(), event.getStartTime(), event.getDuration());

    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }

}
