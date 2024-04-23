package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.api.dto.SponsorDto;
import org.com.ems.controller.api.ISponsorController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.ISponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for CRUD operation for the DAO object {@link Sponsor}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping("/sponsor")
public class SponsorController implements ISponsorController {

	private final ISponsorRepository sponsorRepository;
	private final Function<Sponsor, SponsorDto> sponsorToSponsorDtoConverter;
	private final Function<SponsorDto, Sponsor> sponsorDtoToSponsorConverter;

	public SponsorController(@Autowired final ISponsorRepository sponsorRepository,
			@Autowired @Qualifier("sponsorToSponsorDtoConverter") final Function<Sponsor, SponsorDto> sponsorToSponsorDtoConverter,
			@Autowired @Qualifier("sponsorDtoToSponsorConverter") final Function<SponsorDto, Sponsor> sponsorDtoToSponsorConverter) {

		this.sponsorRepository = requireNonNull(sponsorRepository);
		this.sponsorToSponsorDtoConverter = requireNonNull(sponsorToSponsorDtoConverter);
		this.sponsorDtoToSponsorConverter = requireNonNull(sponsorDtoToSponsorConverter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<SponsorDto> postSponsor(final SponsorDto sponsorDto) {

		final Sponsor sponsor = this.sponsorRepository.save(this.sponsorDtoToSponsorConverter.apply(sponsorDto));
		final SponsorDto newSponsorDto = this.sponsorToSponsorDtoConverter.apply(sponsor);

		try {

			return ResponseEntity.created(new URI("/sponsor/")).body(newSponsorDto);
		} catch (final URISyntaxException e) {

			return new ResponseEntity<>(newSponsorDto, HttpStatus.CREATED);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<SponsorDto> getSponsor(final UUID sponsorId) {

		final var optionalSponsor = this.sponsorRepository.findById(sponsorId);

		final SponsorDto sponsorDto = this.sponsorToSponsorDtoConverter
				.apply(optionalSponsor.orElseThrow(() -> new ObjectNotFoundException(sponsorId, SponsorDto.class)));

		return ResponseEntity.ok(sponsorDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<SponsorDto> putSponsor(final UUID sponsorId, final SponsorDto sponsorDto) {

		if (this.sponsorRepository.existsById(sponsorId)) {

			final Sponsor sponsor = this.sponsorRepository.save(this.sponsorDtoToSponsorConverter.apply(sponsorDto));
			final SponsorDto newSponsorDto = this.sponsorToSponsorDtoConverter.apply(sponsor);

			try {
				return ResponseEntity.created(new URI("/sponsor/" + sponsorId)).body(newSponsorDto);
			} catch (final URISyntaxException e) {

				return new ResponseEntity<>(newSponsorDto, HttpStatus.CREATED);
			}
		}

		throw new ObjectNotFoundException(sponsorId, Sponsor.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<?> deleteSponsor(final UUID sponsorId) {

		if (!this.sponsorRepository.existsById(sponsorId)) {

			throw new ObjectNotFoundException(sponsorId, Sponsor.class);
		}

		this.sponsorRepository.deleteById(sponsorId);

		return ResponseEntity.noContent().build();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Collection<SponsorDto>> getSponsors() {

		final var listOfDtos = this.sponsorRepository.findAll().stream().map(this.sponsorToSponsorDtoConverter::apply)
				.toList();

		return ResponseEntity.ok(listOfDtos);
	}

}
