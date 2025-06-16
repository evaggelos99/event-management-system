package io.github.evaggelos99.ems.attendee.service;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import io.github.evaggelos99.ems.attendee.api.AttendeeTicketMapping;
import io.github.evaggelos99.ems.attendee.api.repo.IAttendeeRepository;
import io.github.evaggelos99.ems.attendee.api.service.IAttendeeService;
import io.github.evaggelos99.ems.attendee.service.remote.EventServicePublisher;
import io.github.evaggelos99.ems.attendee.service.remote.TicketLookUpRemoteService;
import io.github.evaggelos99.ems.attendee.service.repository.AttendeeRepository;
import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.db.IMappingRepository;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.PublisherValidator;
import io.github.evaggelos99.ems.security.lib.SecurityContextHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static io.github.evaggelos99.ems.user.api.Roles.*;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.ObjectUtils.notEqual;

@Service
public class AttendeeService implements IAttendeeService {

    private final IAttendeeRepository attendeeRepository;
    private final EventServicePublisher eventService;
    private final TicketLookUpRemoteService lookUpTicketService;
    private final IMappingRepository<AttendeeTicketMapping> attendeeTicketMappingRepository;
    private final EmailService emailService;

    /**
     * C-or
     *
     * @param attendeeRepository  {@link AttendeeRepository} the
     *                            repository that communicates with the
     *                            database
     *                            that converts Attendee to AttendeeDto
     * @param eventService        the {@link EventServicePublisher} used for
     *                            cascading adding attendee to it's event
     * @param lookUpTicketService the {@link TicketLookUpRemoteService} as a lookup
     *                            for the tickets
     */
    public AttendeeService(final IAttendeeRepository attendeeRepository,
                           final EventServicePublisher eventService,
                           final TicketLookUpRemoteService lookUpTicketService,
                           final IMappingRepository<AttendeeTicketMapping> attendeeTicketMappingRepository,
                           final EmailService emailService) {

        this.attendeeRepository = requireNonNull(attendeeRepository);
        this.eventService = requireNonNull(eventService);
        this.lookUpTicketService = requireNonNull(lookUpTicketService);
        this.attendeeTicketMappingRepository = attendeeTicketMappingRepository;
        this.emailService = emailService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Attendee> add(final AttendeeDto attendee) {

        return SecurityContextHelper.filterRoles(ROLE_CREATE_ATTENDEE)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeRepository.save(attendee)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Attendee> get(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_READ_ATTENDEE)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeRepository.findById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_DELETE_ATTENDEE)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeRepository.deleteById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Attendee> edit(final UUID uuid, final AttendeeDto attendee) {

        return notEqual(uuid, attendee.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, AttendeeDto.class))
                : SecurityContextHelper.filterRoles(ROLE_UPDATE_ATTENDEE)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeRepository.edit(attendee)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Attendee> getAll() {

        return SecurityContextHelper.filterRoles(ROLE_READ_ATTENDEE)
                .flatMapMany(x -> PublisherValidator.validateBooleanFlux(x, attendeeRepository::findAll));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID attendeeId) {

        return SecurityContextHelper.filterRoles(ROLE_READ_ATTENDEE)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeRepository.existsById(attendeeId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> addTicket(final UUID attendeeId, final UUID ticketId) {

        return SecurityContextHelper.filterRoles(ROLE_UPDATE_ATTENDEE)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeTicketMappingRepository.saveSingularMapping(attendeeId, ticketId)))
                .flatMap(x -> lookUpTicketService.lookUpTicket(ticketId))
                .flatMap(ticketDto -> eventService.addAttendee(ticketDto, attendeeId))
                .flatMap(x -> emailService.sendPurchaseTicketEmail(attendeeId, x))
                .map(x -> true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> removeTicket(final UUID attendeeId, final UUID ticketId) {

        return SecurityContextHelper.filterRoles(ROLE_UPDATE_ATTENDEE)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeTicketMappingRepository.deleteSingularMapping(attendeeId, ticketId)))
                .flatMap(x -> lookUpTicketService.lookUpTicket(ticketId))
                .flatMap(ticketDto -> eventService.removeAttendee(ticketDto.eventID(), attendeeId))
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }
}
