package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.UUID;

import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.controller.api.IOrganizerController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IOrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Organizer> postOrganizer(final Organizer organizer) {

		try {
			return ResponseEntity.created(new URI("/attendee/")).body(this.organizerRepository.save(organizer));
		} catch (final URISyntaxException e) {

			return new ResponseEntity<>(this.organizerRepository.save(organizer), HttpStatus.CREATED);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Organizer> getOrganizer(final UUID organizerId) {

		final var optionalOrganizer = this.organizerRepository.findById(organizerId);

		return ResponseEntity.of(optionalOrganizer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Organizer> putOrganizer(final UUID organizerId, final Organizer organizer) {

		if (this.organizerRepository.existsById(organizerId)) {

			try {
				return ResponseEntity.created(new URI("/organizer/" + organizerId))
						.body(this.organizerRepository.save(organizer));
			} catch (final URISyntaxException e) {

				return new ResponseEntity<>(this.organizerRepository.save(organizer), HttpStatus.CREATED);
			}
		}

		throw new ObjectNotFoundException(organizerId, Organizer.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<?> deleteOrganizer(final UUID organizerId) {

		if (!this.organizerRepository.existsById(organizerId)) {

			throw new ObjectNotFoundException(organizerId, Organizer.class);
		}

		this.organizerRepository.deleteById(organizerId);
		return ResponseEntity.noContent().build();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Collection<Organizer>> getOrganizers() {

		return ResponseEntity.ok().body(this.organizerRepository.findAll());
	}

}
