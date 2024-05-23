package org.com.ems.services.impl;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.api.dto.OrganizerDto;
import org.com.ems.db.IOrganizerRepository;
import org.com.ems.db.impl.OrganizerRepository;
import org.com.ems.services.api.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizerService implements IService<Organizer, OrganizerDto> {

    private final IOrganizerRepository organizerRepository;

    /**
     * C-or
     *
     * @param organizerRepository {@link OrganizerRepository} the repository that
     *                            communicates with the database
     */
    public OrganizerService(@Autowired final IOrganizerRepository organizerRepository) {

	this.organizerRepository = requireNonNull(organizerRepository);

    }

    @Override
    public Organizer add(final OrganizerDto attendee) {

	return this.organizerRepository.save(attendee);

    }

    @Override
    public Optional<Organizer> get(final UUID uuid) {

	return this.organizerRepository.findById(uuid);

    }

    @Override
    public void delete(final UUID uuid) {

	this.organizerRepository.deleteById(uuid);

    }

    @Override
    public Organizer edit(final UUID uuid,
			  final OrganizerDto attendee) {

	if (!this.organizerRepository.existsById(uuid))
	    throw new NoSuchElementException();

	return this.organizerRepository.edit(attendee);

    }

    @Override
    public Collection<Organizer> getAll() {

	return this.organizerRepository.findAll();

    }

    @Override
    public boolean existsById(final UUID attendeeId) {

	return this.organizerRepository.existsById(attendeeId);

    }

}
