package io.github.evaggelos99.ems.organizer.service;

import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.PublisherValidator;
import io.github.evaggelos99.ems.common.api.service.IService;
import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.github.evaggelos99.ems.organizer.api.OrganizerDto;
import io.github.evaggelos99.ems.organizer.api.repo.IOrganizerRepository;
import io.github.evaggelos99.ems.security.lib.SecurityContextHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static io.github.evaggelos99.ems.security.lib.Roles.*;
import static java.util.Objects.requireNonNull;

@Service
public class OrganizerService implements IService<Organizer, OrganizerDto> {

    private final IOrganizerRepository organizerRepository;

    /**
     * C-or
     *
     * @param organizerRepository {@link OrganizerRepository} the repository that
     *                            communicates with the database
     */
    public OrganizerService(final IOrganizerRepository organizerRepository) {

        this.organizerRepository = requireNonNull(organizerRepository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Organizer> add(final OrganizerDto organizerDto) {

        return SecurityContextHelper.filterRoles(ROLE_CREATE_ORGANIZER) //TODO extract
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> organizerRepository.save(organizerDto)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Organizer> get(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_READ_ORGANIZER) //TODO extract
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> organizerRepository.findById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_DELETE_ORGANIZER) //TODO extract
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> organizerRepository.deleteById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Organizer> edit(final UUID uuid, final OrganizerDto organizer) {

        return !uuid.equals(organizer.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, OrganizerDto.class))
                : SecurityContextHelper.filterRoles(ROLE_UPDATE_ORGANIZER) //TODO extract
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> organizerRepository.edit(organizer)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Organizer> getAll() {

        return SecurityContextHelper.filterRoles(ROLE_READ_ORGANIZER) //TODO extract
                .flatMapMany(x -> PublisherValidator.validateBooleanFlux(x, organizerRepository::findAll));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID attendeeId) {

        return SecurityContextHelper.filterRoles(ROLE_READ_ORGANIZER) //TODO extract
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> organizerRepository.existsById(attendeeId)));
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }

}
