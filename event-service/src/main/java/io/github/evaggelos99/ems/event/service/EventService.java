package io.github.evaggelos99.ems.event.service;

import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.db.IMappingRepository;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.PublisherValidator;
import io.github.evaggelos99.ems.event.api.*;
import io.github.evaggelos99.ems.event.api.repo.IEventRepository;
import io.github.evaggelos99.ems.event.api.service.IEventService;
import io.github.evaggelos99.ems.event.service.repository.EventRepository;
import io.github.evaggelos99.ems.security.lib.SecurityContextHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

import static io.github.evaggelos99.ems.user.api.Roles.*;

@Service
public class EventService implements IEventService {

    private final IEventRepository eventRepository;
    private final IMappingRepository<EventAttendeeMapping> attendeeEventMappingRepository;
    private final IMappingRepository<EventSponsorMapping> sponsorEventMappingRepository;

    /**
     * C-or
     *
     * @param eventRepository {@link EventRepository} the repository that
     *                        communicates with the database
     */
    public EventService(final IEventRepository eventRepository, final IMappingRepository<EventAttendeeMapping> attendeeEventMappingRepository, final IMappingRepository<EventSponsorMapping> sponsorEventMappingRepository) {

        this.eventRepository = eventRepository;
        this.attendeeEventMappingRepository = attendeeEventMappingRepository;
        this.sponsorEventMappingRepository = sponsorEventMappingRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Event> add(final EventDto event) {

        return SecurityContextHelper.filterRoles(ROLE_CREATE_EVENT)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> eventRepository.save(event)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Event> get(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_READ_EVENT)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> eventRepository.findById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_DELETE_EVENT)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> eventRepository.deleteById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Event> edit(final UUID uuid, final EventDto event) {

        return ObjectUtils.notEqual(uuid, event.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, EventDto.class)) :
                SecurityContextHelper.filterRoles(ROLE_UPDATE_EVENT)
                        .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> eventRepository.edit(event)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Event> getAll() {

        return SecurityContextHelper.filterRoles(ROLE_READ_EVENT)
                .flatMapMany(x -> PublisherValidator.validateBooleanFlux(x, eventRepository::findAll));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID eventId) {

        return SecurityContextHelper.filterRoles(ROLE_READ_EVENT)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> eventRepository.existsById(eventId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> addAttendee(final UUID eventId, final UUID attendeeId) {
        // TODO add role and add whatever is needed

        return SecurityContextHelper.filterRoles(ROLE_UPDATE_EVENT)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeEventMappingRepository.saveSingularMapping(eventId, attendeeId)))
                .map(Objects::nonNull)
                .onErrorReturn(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> removeAttendee(final UUID eventId, final UUID attendeeId) {

        return SecurityContextHelper.filterRoles(ROLE_UPDATE_EVENT)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeEventMappingRepository.deleteSingularMapping(eventId, attendeeId)))
                .onErrorReturn(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> addSponsor(final UUID eventId, final UUID sponsorId) {

        return SecurityContextHelper.filterRoles(ROLE_UPDATE_EVENT)
                .flatMap(x -> sponsorEventMappingRepository.saveSingularMapping(eventId, sponsorId)
                        .map(Objects::nonNull));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> removeSponsor(final UUID eventId, final UUID sponsorId) {

        return SecurityContextHelper.filterRoles(ROLE_UPDATE_EVENT)
                .flatMap(x -> sponsorEventMappingRepository.deleteSingularMapping(eventId, sponsorId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<EventStream> getEventStreams(final UUID eventId) {

        // FIXME add business logic for attendee
        //
        // if attendee is registered to the event
        // if attendee has ticket to event
        // if ticket is eligible for streaming
        // todo

        return SecurityContextHelper.filterRoles(ROLE_READ_EVENT)
                .flatMapMany(x -> PublisherValidator.validateBooleanFlux(x, () -> eventRepository.findAllEventStreams(eventId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }

}
