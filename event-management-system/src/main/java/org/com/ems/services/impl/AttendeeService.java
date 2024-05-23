package org.com.ems.services.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IAttendeeRepository;
import org.com.ems.services.api.IAttendeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Evangelos Georgiou
 *
 */
@Service
public class AttendeeService implements IAttendeeService {

    private final IAttendeeRepository attendeeRepository;
    private final Function<Attendee, AttendeeDto> attendeeToAttendeeDtoConverter;

    public AttendeeService(@Autowired final IAttendeeRepository attendeeRepository,
			   @Autowired @Qualifier("attendeeToAttendeeDtoConverter") final Function<Attendee,
				   AttendeeDto> attendeeToAttendeeDtoConverter) {

	this.attendeeRepository = attendeeRepository;
	this.attendeeToAttendeeDtoConverter = attendeeToAttendeeDtoConverter;

    }

    @Override
    public Attendee add(final AttendeeDto attendee) {

	return this.attendeeRepository.save(attendee);

    }

    @Override
    public Optional<Attendee> get(final UUID uuid) {

	return this.attendeeRepository.findById(uuid);

    }

    @Override
    public void delete(final UUID uuid) {

	this.attendeeRepository.deleteById(uuid);

    }

    @Override
    public Attendee edit(final UUID uuid,
			 final AttendeeDto attendee) {

	if (!this.attendeeRepository.existsById(uuid))
	    throw new NoSuchElementException();

	return this.attendeeRepository.edit(attendee);

    }

    @Override
    public Collection<Attendee> getAll() {

	return this.attendeeRepository.findAll();

    }

    @Override
    public boolean existsById(final UUID attendeeId) {

	return this.attendeeRepository.existsById(attendeeId);

    }

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

	return true;

    }

}
