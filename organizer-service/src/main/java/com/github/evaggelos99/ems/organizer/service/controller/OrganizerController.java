package com.github.evaggelos99.ems.organizer.service.controller;

import static java.util.Objects.requireNonNull;

import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.evaggelos99.ems.common.api.service.IService;
import com.github.evaggelos99.ems.organizer.api.IOrganizerController;
import com.github.evaggelos99.ems.organizer.api.Organizer;
import com.github.evaggelos99.ems.organizer.api.OrganizerDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for CRUD operation for the object {@link Organizer}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping(OrganizerController.ORGANIZER_PATH)
public class OrganizerController implements IOrganizerController {

    static final String ORGANIZER_PATH = "/organizer";
    private final IService<Organizer, OrganizerDto> organizerService;
    private final Function<Organizer, OrganizerDto> organizerToOrganizerDtoConverter;

    /**
     * c-or
     *
     * @param organizerService                 service responsible for CRUD
     *                                         operations
     * @param organizerToOrganizerDtoConverter organizer to DTO
     * @param organizerDtoToOrganizerConverter DTO to organizer
     */
    public OrganizerController(@Autowired final IService<Organizer, OrganizerDto> organizerService,
			       @Autowired @Qualifier("organizerToOrganizerDtoConverter") final Function<Organizer,
				       OrganizerDto> organizerToOrganizerDtoConverter) {

	this.organizerService = requireNonNull(organizerService);
	this.organizerToOrganizerDtoConverter = requireNonNull(organizerToOrganizerDtoConverter);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<OrganizerDto> postOrganizer(final OrganizerDto organizerDto) {

	return this.organizerService.add(organizerDto).map(this.organizerToOrganizerDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<OrganizerDto> getOrganizer(final UUID organizerId) {

	return this.organizerService.get(organizerId).map(this.organizerToOrganizerDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<OrganizerDto> putOrganizer(final UUID organizerId,
					   final OrganizerDto organizerDto) {

	return this.organizerService.edit(organizerId, organizerDto).map(this.organizerToOrganizerDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<?> deleteOrganizer(final UUID organizerId) {

	return this.organizerService.delete(organizerId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<OrganizerDto> getOrganizers() {

	return this.organizerService.getAll().map(this.organizerToOrganizerDtoConverter::apply);

    }

}
