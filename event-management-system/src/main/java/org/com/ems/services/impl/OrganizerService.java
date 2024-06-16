package org.com.ems.services.impl;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.api.dto.OrganizerDto;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IOrganizerRepository;
import org.com.ems.db.impl.OrganizerRepository;
import org.com.ems.services.api.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Organizer> add(final OrganizerDto attendee) {

	return this.organizerRepository.save(attendee);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Organizer> get(final UUID uuid) {

	return this.organizerRepository.findById(uuid);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

	return this.organizerRepository.deleteById(uuid);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Organizer> edit(final UUID uuid,
				final OrganizerDto organizer) {

	return !uuid.equals(organizer.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, OrganizerDto.class))
		: this.organizerRepository.edit(organizer);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Organizer> getAll() {

	return this.organizerRepository.findAll();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID attendeeId) {

	return this.organizerRepository.existsById(attendeeId);

    }

}
