package io.github.evaggelos99.ems.organizer.service.controller;

import io.github.evaggelos99.ems.common.api.service.IService;
import io.github.evaggelos99.ems.organizer.api.IOrganizerController;
import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.github.evaggelos99.ems.organizer.api.OrganizerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

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
     * @param organizerToOrganizerDtoConverter DTO to organizer
     */
    public OrganizerController(final IService<Organizer, OrganizerDto> organizerService, @Qualifier("organizerToOrganizerDtoConverter") final Function<Organizer, OrganizerDto> organizerToOrganizerDtoConverter) {

        this.organizerService = requireNonNull(organizerService);
        this.organizerToOrganizerDtoConverter = requireNonNull(organizerToOrganizerDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> deleteOrganizer(final UUID organizerId) {

        return organizerService.delete(organizerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<OrganizerDto> getOrganizer(final UUID organizerId) {

        return organizerService.get(organizerId).map(organizerToOrganizerDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<OrganizerDto> getOrganizers() {

        return organizerService.getAll().map(organizerToOrganizerDtoConverter);

    }

    @Override
    public Mono<Boolean> ping() {

        return pingService();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<OrganizerDto> postOrganizer(final OrganizerDto organizerDto) {

        return organizerService.add(organizerDto).map(organizerToOrganizerDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<OrganizerDto> putOrganizer(final UUID organizerId, final OrganizerDto organizerDto) {

        return organizerService.edit(organizerId, organizerDto).map(organizerToOrganizerDtoConverter);
    }

    private Mono<Boolean> pingService() {

        try {

            return organizerService.ping().onErrorReturn(false);
        } catch (final Exception e) {

            return Mono.just(false);
        }
    }

}
