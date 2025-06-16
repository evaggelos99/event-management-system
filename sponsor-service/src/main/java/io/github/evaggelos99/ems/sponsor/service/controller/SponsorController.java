package io.github.evaggelos99.ems.sponsor.service.controller;

import io.github.evaggelos99.ems.common.api.service.IService;
import io.github.evaggelos99.ems.sponsor.api.ISponsorController;
import io.github.evaggelos99.ems.sponsor.api.Sponsor;
import io.github.evaggelos99.ems.sponsor.api.SponsorDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

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
    public SponsorController(final IService<Sponsor, SponsorDto> sponsorService,
                             @Qualifier("sponsorToSponsorDtoConverter") final Function<Sponsor, SponsorDto> sponsorToSponsorDtoConverter) {

        this.sponsorService = requireNonNull(sponsorService);
        this.sponsorToSponsorDtoConverter = requireNonNull(sponsorToSponsorDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<SponsorDto> postSponsor(final SponsorDto sponsorDto) {

        return sponsorService.add(sponsorDto).map(sponsorToSponsorDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<SponsorDto> getSponsor(final UUID sponsorId) {

        return sponsorService.get(sponsorId).map(sponsorToSponsorDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<SponsorDto> getSponsors() {

        return sponsorService.getAll().map(sponsorToSponsorDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<SponsorDto> putSponsor(final UUID sponsorId, final SponsorDto sponsorDto) {

        return sponsorService.edit(sponsorId, sponsorDto).map(sponsorToSponsorDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<ResponseEntity<Void>> deleteSponsor(final UUID sponsorId) {

        return sponsorService.delete(sponsorId).filter(Boolean::booleanValue).map(this::mapResponseEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<ResponseEntity<Void>> ping() {

        return sponsorService.ping().filter(Boolean::booleanValue).map(x -> ResponseEntity.ok().build());
    }

    private ResponseEntity<Void> mapResponseEntity(Boolean ignored) {
        return ResponseEntity.ok().build();
    }

}
