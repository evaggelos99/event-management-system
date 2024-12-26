package io.github.evaggelos99.ems.sponsor.service;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.service.IService;
import io.github.evaggelos99.ems.sponsor.api.Sponsor;
import io.github.evaggelos99.ems.sponsor.api.SponsorDto;
import io.github.evaggelos99.ems.sponsor.api.repo.ISponsorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SponsorService implements IService<Sponsor, SponsorDto> {

	private final ISponsorRepository sponsorRepository;

	/**
	 * C-or
	 *
	 * @param organizerRepository {@link SponsorRepository} the repository that
	 *                            communicates with the database
	 */
	public SponsorService(@Autowired final ISponsorRepository sponsorRepository) {

		this.sponsorRepository = requireNonNull(sponsorRepository);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<Sponsor> add(final SponsorDto attendee) {

		return sponsorRepository.save(attendee);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<Sponsor> get(final UUID uuid) {

		return sponsorRepository.findById(uuid);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<Boolean> delete(final UUID uuid) {

		return sponsorRepository.deleteById(uuid);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<Sponsor> edit(final UUID uuid, final SponsorDto sponsor) {

		return !uuid.equals(sponsor.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, SponsorDto.class))
				: sponsorRepository.edit(sponsor);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<Sponsor> getAll() {

		return sponsorRepository.findAll();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<Boolean> existsById(final UUID attendeeId) {

		return sponsorRepository.existsById(attendeeId);

	}

	@Override
	public Mono<Boolean> ping() {

		return Mono.just(true);
	}

}
