package io.github.evaggelos99.ems.ticket.service;

import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.PublisherValidator;
import io.github.evaggelos99.ems.common.api.service.IService;
import io.github.evaggelos99.ems.security.lib.SecurityContextHelper;
import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import io.github.evaggelos99.ems.ticket.api.repo.ITicketRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static io.github.evaggelos99.ems.security.lib.Roles.*;
import static java.util.Objects.requireNonNull;

@Service
public class TicketService implements IService<Ticket, TicketDto> {

    private final ITicketRepository ticketRepository;

    /**
     * C-or
     *
     * @param ticketRepository {@link TicketRepository} the repository that
     *                         communicates with the database
     */
    public TicketService(final ITicketRepository ticketRepository) {

        this.ticketRepository = requireNonNull(ticketRepository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Ticket> add(final TicketDto ticketDto) {

        return SecurityContextHelper.filterRoles(ROLE_CREATE_TICKET) //TODO extract
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> ticketRepository.save(ticketDto)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Ticket> get(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_READ_TICKET) //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> ticketRepository.findById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_DELETE_TICKET) //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> ticketRepository.deleteById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Ticket> edit(final UUID uuid, final TicketDto ticketDto) {

        return !uuid.equals(ticketDto.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, TicketDto.class))
                : SecurityContextHelper.filterRoles(ROLE_UPDATE_TICKET) //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> ticketRepository.edit(ticketDto)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Ticket> getAll() {

        return SecurityContextHelper.filterRoles(ROLE_READ_TICKET) //TODO extract 
                .flatMapMany(x -> PublisherValidator.validateBooleanFlux(x, ticketRepository::findAll));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID ticketId) {

        return SecurityContextHelper.filterRoles(ROLE_READ_TICKET) //TODO extract 
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> ticketRepository.existsById(ticketId)));
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }

}
