package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.com.ems.api.dao.Sponsor;
import org.com.ems.controller.api.ISponsorController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.controller.utils.CommonControllerUtils;
import org.com.ems.db.ISponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
	public Sponsor postSponsor(final Sponsor sponsor) {

		return this.sponsorRepository.save(sponsor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Sponsor getSponsor(final String sponsorId) {

		final var uuid = CommonControllerUtils.stringToUUID(sponsorId);
		final var optionalSponsor = this.sponsorRepository.findById(uuid);

		return optionalSponsor.orElseThrow(() -> new ObjectNotFoundException(uuid, Sponsor.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Sponsor putSponsor(final String sponsorId, final Sponsor sponsor) {

		final var uuid = CommonControllerUtils.stringToUUID(sponsorId);

		if (this.sponsorRepository.existsById(uuid)) {

			return this.sponsorRepository.save(sponsor);
		}

		throw new ObjectNotFoundException(uuid, Sponsor.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteSponsor(final String sponsorId) {

		this.sponsorRepository.deleteById(CommonControllerUtils.stringToUUID(sponsorId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Sponsor> getSponsors() {

		return this.sponsorRepository.findAll();
	}

}
