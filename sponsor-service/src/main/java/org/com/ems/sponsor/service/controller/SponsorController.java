package org.com.ems.sponsor.service.controller;

import static java.util.Objects.requireNonNull;

import java.util.UUID;
import java.util.function.Function;

import org.com.ems.common.api.service.IService;
import org.com.ems.sponsor.api.ISponsorController;
import org.com.ems.sponsor.api.Sponsor;
import org.com.ems.sponsor.api.SponsorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for CRUD operation for the object {@link Sponsor}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping(SponsorController.SPONSOR_PATH)
public class SponsorController implements ISponsorController {

    static final String SPONSOR_PATH = "/sponsor";
    private final IService<Sponsor, SponsorDto> sponsorService;
    private final Function<Sponsor, SponsorDto> sponsorToSponsorDtoConverter;

    /**
     * C-or
     *
     * @param sponsorService               service responsible for CRUD operations
     * @param sponsorToSponsorDtoConverter sponsor to DTO
     */
    public SponsorController(@Autowired final IService<Sponsor, SponsorDto> sponsorService,
			     @Autowired @Qualifier("sponsorToSponsorDtoConverter") final Function<Sponsor,
				     SponsorDto> sponsorToSponsorDtoConverter) {

	this.sponsorService = requireNonNull(sponsorService);
	this.sponsorToSponsorDtoConverter = requireNonNull(sponsorToSponsorDtoConverter);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<SponsorDto> postSponsor(final SponsorDto sponsorDto) {

	return this.sponsorService.add(sponsorDto).map(this.sponsorToSponsorDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<SponsorDto> getSponsor(final UUID sponsorId) {

	return this.sponsorService.get(sponsorId).map(this.sponsorToSponsorDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<SponsorDto> putSponsor(final UUID sponsorId,
				       final SponsorDto sponsorDto) {

	return this.sponsorService.edit(sponsorId, sponsorDto).map(this.sponsorToSponsorDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> deleteSponsor(final UUID sponsorId) {

	return this.sponsorService.delete(sponsorId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<SponsorDto> getSponsors() {

	return this.sponsorService.getAll().map(this.sponsorToSponsorDtoConverter::apply);

    }

}
