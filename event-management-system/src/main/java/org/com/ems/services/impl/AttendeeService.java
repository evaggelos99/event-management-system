package org.com.ems.services.impl;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.converters.AttendeeToAttendeeDtoConverter;
import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IAttendeeRepository;
import org.com.ems.db.impl.AttendeeRepository;
import org.com.ems.services.api.IAttendeeService;
import org.com.ems.services.api.IEventService;
import org.com.ems.services.api.ILookUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AttendeeService implements IAttendeeService {

    private final IAttendeeRepository attendeeRepository;
    private final Function<Attendee, AttendeeDto> attendeeToAttendeeDtoConverter;
    private final IEventService eventService;
    private final ILookUpService<Ticket> lookUpTicketService;

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
			   @Autowired final IEventService eventService,
			   @Autowired @Qualifier("ticketService") final ILookUpService<Ticket> lookUpTicketService) {

	this.attendeeRepository = requireNonNull(attendeeRepository);
	this.attendeeToAttendeeDtoConverter = requireNonNull(attendeeToAttendeeDtoConverter);
	this.eventService = requireNonNull(eventService);
	this.lookUpTicketService = requireNonNull(lookUpTicketService);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attendee add(final AttendeeDto attendee) {

	return this.attendeeRepository.save(attendee);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Attendee> get(final UUID uuid) {

	return this.attendeeRepository.findById(uuid);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final UUID uuid) {

	this.attendeeRepository.deleteById(uuid);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attendee edit(final UUID uuid,
			 final AttendeeDto attendee) {

	if (!this.attendeeRepository.existsById(uuid))
	    throw new NoSuchElementException();

	return this.attendeeRepository.edit(attendee);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Attendee> getAll() {

	return this.attendeeRepository.findAll();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(final UUID attendeeId) {

	return this.attendeeRepository.existsById(attendeeId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addTicket(final UUID attendeeId,
			     final UUID ticketId) {

	final Optional<Attendee> optionalAttendee = this.attendeeRepository.findById(attendeeId);

	final Attendee attendee = optionalAttendee
		.orElseThrow(() -> new ObjectNotFoundException(attendeeId, AttendeeDto.class));

	final List<UUID> ids = attendee.getTicketIDs();

	final LinkedList<UUID> list = new LinkedList<>(ids);
	list.add(ticketId);

	final Attendee newEvent = new Attendee(attendee.getUuid(), attendee.getCreatedAt(), attendee.getLastUpdated(),
		attendee.getFirstName(), attendee.getLastName(), list);

	final AttendeeDto dto = this.attendeeToAttendeeDtoConverter.apply(newEvent);

	final Attendee attendeeFromRepo = this.attendeeRepository.edit(dto);

	if (!attendeeFromRepo.getTicketIDs().containsAll(list)) {

	    return false;
	}

	final Ticket ticket = this.lookUpTicketService.get(ticketId)
		.orElseThrow(() -> new ObjectNotFoundException(ticketId, Ticket.class));

	return this.eventService.addAttendee(ticket.getEventID(), attendeeId);

    }

}
