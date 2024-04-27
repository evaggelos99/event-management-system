package org.com.ems.services;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.db.IAttendeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendeeService implements IService<Attendee> {

    private final IAttendeeRepository attendeeRepository;

    public AttendeeService(@Autowired final IAttendeeRepository attendeeRepository) {

	this.attendeeRepository = attendeeRepository;

    }

    @Override
    public Attendee add(final Attendee attendee) {

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
			 final Attendee attendee) {

	if (!this.attendeeRepository.existsById(uuid))
	    throw new NoSuchElementException();

	return this.attendeeRepository.save(attendee);

    }

    @Override
    public Collection<Attendee> getAll() {

	return this.attendeeRepository.findAll();

    }

    @Override
    public boolean existsById(final UUID attendeeId) {

	return this.attendeeRepository.existsById(attendeeId);

    }

}
