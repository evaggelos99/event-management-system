package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.UUID;

import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.controller.api.ISponsorController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.ISponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

	public SponsorController(@Autowired final ISponsorRepository sponsorRepository) {

		this.sponsorRepository = requireNonNull(sponsorRepository);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Sponsor> postSponsor(final Sponsor sponsor) {

		try {
			return ResponseEntity.created(new URI("/sponsor/")).body(this.sponsorRepository.save(sponsor));
		} catch (final URISyntaxException e) {

			return new ResponseEntity<>(this.sponsorRepository.save(sponsor), HttpStatus.CREATED);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Sponsor> getSponsor(final UUID sponsorId) {

		final var optionalSponsor = this.sponsorRepository.findById(sponsorId);

		return ResponseEntity.of(optionalSponsor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Sponsor> putSponsor(final UUID sponsorId, final Sponsor sponsor) {

		if (this.sponsorRepository.existsById(sponsorId)) {

			try {
				return ResponseEntity.created(new URI("/sponsor/" + sponsorId))
						.body(this.sponsorRepository.save(sponsor));
			} catch (final URISyntaxException e) {

				return new ResponseEntity<>(this.sponsorRepository.save(sponsor), HttpStatus.CREATED);
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
	public ResponseEntity<Collection<Sponsor>> getSponsors() {

		return ResponseEntity.ofNullable(this.sponsorRepository.findAll());
	}

}
