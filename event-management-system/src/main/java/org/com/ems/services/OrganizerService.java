package org.com.ems.services;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.db.IOrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizerService implements IService<Organizer> {

	private final IOrganizerRepository organizerRepository;

	public OrganizerService(@Autowired final IOrganizerRepository organizerRepository) {

		this.organizerRepository = organizerRepository;
	}

	@Override
	public Organizer add(final Organizer attendee) {

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
	public Organizer edit(final UUID uuid, final Organizer attendee) {

		if (!this.organizerRepository.existsById(uuid))
			throw new NoSuchElementException();

		return this.organizerRepository.save(attendee);
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
