package io.github.evaggelos99.ems.sponsor.service;

import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.PublisherValidator;
import io.github.evaggelos99.ems.common.api.service.IService;
import io.github.evaggelos99.ems.security.lib.SecurityContextHelper;
import io.github.evaggelos99.ems.sponsor.api.Sponsor;
import io.github.evaggelos99.ems.sponsor.api.SponsorDto;
import io.github.evaggelos99.ems.sponsor.api.repo.ISponsorRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static io.github.evaggelos99.ems.security.lib.Roles.*;
import static java.util.Objects.requireNonNull;

@Service
public class SponsorService implements IService<Sponsor, SponsorDto> {

    private final ISponsorRepository sponsorRepository;

    /**
     * C-or
     *
     * @param sponsorRepository {@link SponsorRepository} the repository that
     *                            communicates with the database
     */
    public SponsorService(final ISponsorRepository sponsorRepository) {

        this.sponsorRepository = requireNonNull(sponsorRepository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Sponsor> add(final SponsorDto sponsorDto) {

        return SecurityContextHelper.filterRoles(ROLE_CREATE_SPONSOR) //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> sponsorRepository.save(sponsorDto)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Sponsor> get(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_READ_SPONSOR) //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> sponsorRepository.findById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_DELETE_SPONSOR) //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> sponsorRepository.deleteById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Sponsor> edit(final UUID uuid, final SponsorDto sponsor) {

        return !uuid.equals(sponsor.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, SponsorDto.class))
                : SecurityContextHelper.filterRoles(ROLE_UPDATE_SPONSOR) //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> sponsorRepository.edit(sponsor)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Sponsor> getAll() {

        return SecurityContextHelper.filterRoles(ROLE_READ_SPONSOR) //TODO extract 
                .flatMapMany(x -> PublisherValidator.validateBooleanFlux(x, sponsorRepository::findAll));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID attendeeId) {

        return SecurityContextHelper.filterRoles(ROLE_READ_SPONSOR) //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> sponsorRepository.existsById(attendeeId)));
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }

}
