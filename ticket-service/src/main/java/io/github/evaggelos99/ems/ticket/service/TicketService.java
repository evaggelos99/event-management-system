package io.github.evaggelos99.ems.ticket.service;

import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.PublisherValidator;
import io.github.evaggelos99.ems.security.lib.SecurityContextHelper;
import io.github.evaggelos99.ems.ticket.api.ITicketService;
import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import io.github.evaggelos99.ems.ticket.api.converters.TicketToTicketDtoConverter;
import io.github.evaggelos99.ems.ticket.api.repo.ITicketRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static io.github.evaggelos99.ems.user.api.Roles.*;

@Service
public class TicketService implements ITicketService {

    private final ITicketRepository ticketRepository;
    private final Function<Ticket, TicketDto> ticketToTicketDtoConverter;

    /**
     * @param ticketRepository           {@link TicketRepository} the repository that
     *                                   communicates with the database
     * @param ticketToTicketDtoConverter {@link TicketToTicketDtoConverter} ticket to DTO converter
     */
    public TicketService(final ITicketRepository ticketRepository,
                         @Qualifier("ticketToTicketDtoConverter") final Function<Ticket, TicketDto> ticketToTicketDtoConverter) {

        this.ticketRepository = ticketRepository;
        this.ticketToTicketDtoConverter = ticketToTicketDtoConverter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Ticket> add(final TicketDto ticketDto) {

        return SecurityContextHelper.filterRoles(ROLE_CREATE_TICKET)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> ticketRepository.save(ticketDto)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Ticket> get(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_READ_TICKET)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> ticketRepository.findById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_DELETE_TICKET)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> ticketRepository.deleteById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Ticket> edit(final UUID uuid, final TicketDto ticketDto) {

        return !uuid.equals(ticketDto.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, TicketDto.class))
                : SecurityContextHelper.filterRoles(ROLE_UPDATE_TICKET)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> ticketRepository.edit(ticketDto)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Ticket> getAll() {

        return SecurityContextHelper.filterRoles(ROLE_READ_TICKET)
                .flatMapMany(x -> PublisherValidator.validateBooleanFlux(x, ticketRepository::findAll));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID ticketId) {

        return SecurityContextHelper.filterRoles(ROLE_READ_TICKET)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> ticketRepository.existsById(ticketId)));
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }

    @Override
    public Mono<FileSystemResource> picture(final UUID ticketId) {

        throw new NotImplementedException();
    }

    @Override
    public Mono<Boolean> useTicket(final UUID ticketId) {

        return get(ticketId)
                .map(ticketToTicketDtoConverter)
                .map(dto -> edit(ticketId, TicketDto.from(dto).used(true).build()))
                .map(Objects::nonNull);
    }
}
