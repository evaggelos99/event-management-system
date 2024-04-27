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
import org.com.ems.services.IService;
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

    private final IService<Sponsor> sponsorService;
    private final Function<Sponsor, SponsorDto> sponsorToSponsorDtoConverter;
    private final Function<SponsorDto, Sponsor> sponsorDtoToSponsorConverter;

    /**
     * C-or
     *
     * @param sponsorService               service responsible for CRUD operations
     * @param sponsorToSponsorDtoConverter sponsor to DTO
     * @param sponsorDtoToSponsorConverter DTO to sponsor
     */
    public SponsorController(@Autowired final IService<Sponsor> sponsorService,
			     @Autowired @Qualifier("sponsorToSponsorDtoConverter") final Function<Sponsor,
				     SponsorDto> sponsorToSponsorDtoConverter,
			     @Autowired @Qualifier("sponsorDtoToSponsorConverter") final Function<SponsorDto,
				     Sponsor> sponsorDtoToSponsorConverter) {

	this.sponsorService = requireNonNull(sponsorService);
	this.sponsorToSponsorDtoConverter = requireNonNull(sponsorToSponsorDtoConverter);
	this.sponsorDtoToSponsorConverter = requireNonNull(sponsorDtoToSponsorConverter);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<SponsorDto> postSponsor(final SponsorDto sponsorDto) {

	final Sponsor sponsor = this.sponsorService.add(this.sponsorDtoToSponsorConverter.apply(sponsorDto));
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

	final var optionalSponsor = this.sponsorService.get(sponsorId);

	final SponsorDto sponsorDto = this.sponsorToSponsorDtoConverter
		.apply(optionalSponsor.orElseThrow(() -> new ObjectNotFoundException(sponsorId, SponsorDto.class)));

	return ResponseEntity.ok(sponsorDto);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<SponsorDto> putSponsor(final UUID sponsorId,
						 final SponsorDto sponsorDto) {

	final Sponsor sponsor = this.sponsorService.edit(sponsorId,
		this.sponsorDtoToSponsorConverter.apply(sponsorDto));
	final SponsorDto newSponsorDto = this.sponsorToSponsorDtoConverter.apply(sponsor);

	try {

	    return ResponseEntity.created(new URI("/sponsor/" + sponsorId)).body(newSponsorDto);
	} catch (final URISyntaxException e) {

	    return new ResponseEntity<>(newSponsorDto, HttpStatus.CREATED);
	}

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<?> deleteSponsor(final UUID sponsorId) {

	this.sponsorService.delete(sponsorId);

	return ResponseEntity.noContent().build();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Collection<SponsorDto>> getSponsors() {

	final var listOfDtos = this.sponsorService.getAll().stream().map(this.sponsorToSponsorDtoConverter::apply)
		.toList();

	return ResponseEntity.ok(listOfDtos);

    }

}
