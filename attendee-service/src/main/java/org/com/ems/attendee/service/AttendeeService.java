package org.com.ems.attendee.service;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.attendee.api.Attendee;
import org.com.ems.attendee.api.AttendeeDto;
import org.com.ems.attendee.api.converters.AttendeeToAttendeeDtoConverter;
import org.com.ems.attendee.api.repo.IAttendeeRepository;
import org.com.ems.attendee.api.service.IAttendeeService;
import org.com.ems.attendee.service.remote.EventServiceClient;
import org.com.ems.attendee.service.remote.TicketLookUpServiceClient;
import org.com.ems.common.api.controller.exceptions.DuplicateTicketIdInAttendeeException;
import org.com.ems.common.api.controller.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AttendeeService implements IAttendeeService {

    private final IAttendeeRepository attendeeRepository;
    private final Function<Attendee, AttendeeDto> attendeeToAttendeeDtoConverter;
    private final EventServiceClient eventService;
    private final TicketLookUpServiceClient lookUpTicketService;

    /**
     * C-or
     *
     * @param attendeeRepository             {@link AttendeeRepository} the
     *                                       repository that communicates with the
     *                                       database
     * @param attendeeToAttendeeDtoConverter the
     *                                       {@link AttendeeToAttendeeDtoConverter}
     *                                       that converts Attendee to AttendeeDto
     * @param eventService                   the {@link EventService} used for
     *                                       cascading adding attendee to it's event
     * @param lookUpTicketService            the {@link TicketService} as a lookup
     *                                       for the tickets
     */
    public AttendeeService(@Autowired final IAttendeeRepository attendeeRepository,
			   @Autowired @Qualifier("attendeeToAttendeeDtoConverter") final Function<Attendee,
				   AttendeeDto> attendeeToAttendeeDtoConverter,
			   @Autowired final EventServiceClient eventService,
			   @Autowired final TicketLookUpServiceClient lookUpTicketService) {

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

	return this.attendeeRepository.save(attendee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Attendee> get(final UUID uuid) {

	return this.attendeeRepository.findById(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

	return this.attendeeRepository.deleteById(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Attendee> edit(final UUID uuid,
			       final AttendeeDto attendee) {

	return !uuid.equals(attendee.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, AttendeeDto.class))
		: this.attendeeRepository.edit(attendee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Attendee> getAll() {

	return this.attendeeRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID attendeeId) {

	return this.attendeeRepository.existsById(attendeeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> addTicket(final UUID attendeeId,
				   final UUID ticketId) {

	final Mono<Attendee> monoAttendee = this.attendeeRepository.findById(attendeeId);
	return monoAttendee.map(x -> this.addTicketIdToExistingList(attendeeId, ticketId, x))
		.map(this.attendeeToAttendeeDtoConverter::apply)//
		.flatMap(this.attendeeRepository::edit)//
		.flatMap(x -> this.lookUpTicketService.lookUpTicket(ticketId))//
		.flatMap(x -> this.eventService.addAttendee(x.eventID(), attendeeId)).log().defaultIfEmpty(false);
    }

    private Attendee addTicketIdToExistingList(final UUID attendeeId,
					       final UUID ticketId,
					       final Attendee x) {

//	validateTicketIds();
	final List<UUID> list = x.getTicketIDs();

	if (list.stream().noneMatch(ticketId::equals)) {
	    final LinkedList<UUID> newList = new LinkedList<>(list);
	    newList.add(ticketId);
	    return new Attendee(attendeeId, x.getCreatedAt(), Instant.now(), x.getFirstName(), x.getLastName(),
		    newList);
	}
	throw new DuplicateTicketIdInAttendeeException(ticketId);
    }
}
