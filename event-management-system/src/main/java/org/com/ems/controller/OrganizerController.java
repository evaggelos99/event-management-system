package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.api.dto.OrganizerDto;
import org.com.ems.controller.api.IOrganizerController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IOrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	private final Function<OrganizerDto, Organizer> organizerDtoToOrganizerConverter;
	private final Function<Organizer, OrganizerDto> organizerToOrganizerDtoConverter;

	public OrganizerController(@Autowired final IOrganizerRepository organizerRepository,
			@Autowired @Qualifier("organizerToOrganizerDtoConverter") final Function<Organizer, OrganizerDto> organizerToOrganizerDtoConverter,
			@Autowired @Qualifier("organizerDtoToOrganizerConverter") final Function<OrganizerDto, Organizer> organizerDtoToOrganizerConverter) {

		this.organizerRepository = requireNonNull(organizerRepository);
		this.organizerDtoToOrganizerConverter = requireNonNull(organizerDtoToOrganizerConverter);
		this.organizerToOrganizerDtoConverter = requireNonNull(organizerToOrganizerDtoConverter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<OrganizerDto> postOrganizer(final OrganizerDto organizerDto) {

		final Organizer organizer = this.organizerRepository
				.save(this.organizerDtoToOrganizerConverter.apply(organizerDto));

		final OrganizerDto newDto = this.organizerToOrganizerDtoConverter.apply(organizer);

		try {
			return ResponseEntity.created(new URI("/attendee/")).body(newDto);
		} catch (final URISyntaxException e) {

			return new ResponseEntity<>(newDto, HttpStatus.CREATED);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<OrganizerDto> getOrganizer(final UUID organizerId) {

		final var optionalOrganizer = this.organizerRepository.findById(organizerId);

		final OrganizerDto organizerDto = this.organizerToOrganizerDtoConverter.apply(
				optionalOrganizer.orElseThrow(() -> new ObjectNotFoundException(organizerId, OrganizerDto.class)));

		return ResponseEntity.ok(organizerDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<OrganizerDto> putOrganizer(final UUID organizerId, final OrganizerDto organizerDto) {

		if (this.organizerRepository.existsById(organizerId)) {

			final Organizer organizer = this.organizerRepository
					.save(this.organizerDtoToOrganizerConverter.apply(organizerDto));

			final OrganizerDto newDto = this.organizerToOrganizerDtoConverter.apply(organizer);

			try {
				return ResponseEntity.created(new URI("/organizer/" + organizerId)).body(newDto);
			} catch (final URISyntaxException e) {

				return new ResponseEntity<>(newDto, HttpStatus.CREATED);
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
	public ResponseEntity<Collection<OrganizerDto>> getOrganizers() {

		final List<OrganizerDto> listOfDtos = this.organizerRepository.findAll().stream()
				.map(this.organizerToOrganizerDtoConverter::apply).toList();

		return ResponseEntity.ok(listOfDtos);
	}

}
