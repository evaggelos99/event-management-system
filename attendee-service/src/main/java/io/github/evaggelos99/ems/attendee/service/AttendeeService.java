package io.github.evaggelos99.ems.attendee.service;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import io.github.evaggelos99.ems.attendee.api.converters.AttendeeToAttendeeDtoConverter;
import io.github.evaggelos99.ems.attendee.api.repo.IAttendeeRepository;
import io.github.evaggelos99.ems.attendee.api.service.IAttendeeService;
import io.github.evaggelos99.ems.attendee.service.remote.EventServicePublisher;
import io.github.evaggelos99.ems.attendee.service.remote.TicketLookUpRemoteService;
import io.github.evaggelos99.ems.common.api.controller.exceptions.DuplicateTicketIdInAttendeeException;
import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.PublisherValidator;
import io.github.evaggelos99.ems.security.lib.SecurityContextHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.ObjectUtils.notEqual;

@Service
public class AttendeeService implements IAttendeeService {

    private final IAttendeeRepository attendeeRepository;
    private final Function<Attendee, AttendeeDto> attendeeToAttendeeDtoConverter;
    private final EventServicePublisher eventService;
    private final TicketLookUpRemoteService lookUpTicketService;

    /**
     * C-or
     *
     * @param attendeeRepository             {@link AttendeeRepository} the
     *                                       repository that communicates with the
     *                                       database
     * @param attendeeToAttendeeDtoConverter the
     *                                       {@link AttendeeToAttendeeDtoConverter}
     *                                       that converts Attendee to AttendeeDto
     * @param eventService                   the {@link EventServicePublisher} used for
     *                                       cascading adding attendee to it's event
     * @param lookUpTicketService            the {@link TicketLookUpRemoteService} as a lookup
     *                                       for the tickets
     */
    public AttendeeService(final IAttendeeRepository attendeeRepository,
                           @Qualifier("attendeeToAttendeeDtoConverter") final Function<Attendee, AttendeeDto> attendeeToAttendeeDtoConverter,
                           final EventServicePublisher eventService,
                           final TicketLookUpRemoteService lookUpTicketService) {

        this.attendeeRepository = requireNonNull(attendeeRepository);
        this.attendeeToAttendeeDtoConverter = requireNonNull(attendeeToAttendeeDtoConverter);
        this.eventService = requireNonNull(eventService);
        this.lookUpTicketService = requireNonNull(lookUpTicketService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Attendee> add(final AttendeeDto attendee) {

        return SecurityContextHelper.filterRoles("ROLE_CREATE_ATTENDEE") //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeRepository.save(attendee)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Attendee> get(final UUID uuid) {

        return SecurityContextHelper.filterRoles("ROLE_READ_ATTENDEE") //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeRepository.findById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

        return SecurityContextHelper.filterRoles("ROLE_DELETE_ATTENDEE") //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeRepository.deleteById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Attendee> edit(final UUID uuid, final AttendeeDto attendee) {

        return notEqual(uuid, attendee.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, AttendeeDto.class))
                : SecurityContextHelper.filterRoles("ROLE_UPDATE_ATTENDEE") //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeRepository.edit(attendee)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Attendee> getAll() {

        return SecurityContextHelper.filterRoles("ROLE_READ_ATTENDEE") //TODO extract 
                .flatMapMany(x -> PublisherValidator.validateBooleanFlux(x, attendeeRepository::findAll));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID attendeeId) {

        return SecurityContextHelper.filterRoles("ROLE_READ_ATTENDEE") //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> attendeeRepository.findById(attendeeId)))
                .hasElement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> addTicket(final UUID attendeeId, final UUID ticketId) {
        // FIXME add role and investigate if we can propagate token
        return lookUpTicketService.ping().filter(Boolean.TRUE::equals)
                .flatMap(x -> eventService.ping()).filter(Boolean.TRUE::equals)
                .flatMap(x -> attendeeRepository.findById(attendeeId))
                .map(attendee -> addTicketIdToExistingList(attendeeId, ticketId, attendee))
                .map(attendeeToAttendeeDtoConverter)//
                .flatMap(attendeeRepository::edit)//
                .flatMap(x -> lookUpTicketService.lookUpTicket(ticketId))
                .flatMap(ticketDto -> eventService.addAttendee(ticketDto.eventID(), attendeeId)).defaultIfEmpty(false);
    }

    private Attendee addTicketIdToExistingList(final UUID attendeeId, final UUID ticketId, final Attendee attendee) {

        final List<UUID> list = attendee.getTicketIDs();
        if (list.stream().noneMatch(ticketId::equals)) {

            final LinkedList<UUID> newList = new LinkedList<>(list);
            newList.add(ticketId);

            return new Attendee(attendeeId, attendee.getCreatedAt(), Instant.now(), attendee.getFirstName(),
                    attendee.getLastName(), newList);
        }

        throw new DuplicateTicketIdInAttendeeException(ticketId);
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }
}
