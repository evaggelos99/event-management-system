package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.UUID;

import org.com.ems.api.dao.Organizer;
import org.com.ems.controller.api.IOrganizerController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.controller.utils.CommonControllerUtils;
import org.com.ems.db.IOrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for CRUD operation for the DAO object {@link Organizer}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping("/organizer")
public class OrganizerController implements IOrganizerController {

	private final IOrganizerRepository organizerRepository;

	public OrganizerController(@Autowired final IOrganizerRepository organizerRepository) {

		this.organizerRepository = requireNonNull(organizerRepository);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Organizer postOrganizer(final Organizer organizer) {

		return this.organizerRepository.save(organizer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Organizer getOrganizer(final String organizerId) {

		final UUID uuid = CommonControllerUtils.stringToUUID(organizerId);
		return this.organizerRepository.findById(uuid)
				.orElseThrow(() -> new ObjectNotFoundException(uuid, Organizer.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Organizer putOrganizer(final String organizerId, final Organizer organizer) {

		final UUID uuid = CommonControllerUtils.stringToUUID(organizerId);
		if (this.organizerRepository.existsById(uuid)) {
			return this.organizerRepository.save(organizer);
		}

		throw new ObjectNotFoundException(uuid, Organizer.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteOrganizer(final String organizerId) {

		this.organizerRepository.deleteById(CommonControllerUtils.stringToUUID(organizerId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Organizer> getOrganizers() {

		return this.organizerRepository.findAll();
	}

}
