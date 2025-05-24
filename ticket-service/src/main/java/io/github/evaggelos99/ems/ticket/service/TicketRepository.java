package io.github.evaggelos99.ems.ticket.service;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.SeatingInformation;
import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import io.github.evaggelos99.ems.ticket.api.converters.TicketDtoToTicketConverter;
import io.github.evaggelos99.ems.ticket.api.repo.ITicketRepository;
import io.github.evaggelos99.ems.ticket.api.repo.TicketRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Component
public class TicketRepository implements ITicketRepository {

    private final DatabaseClient databaseClient;
    private final TicketRowMapper ticketRowMapper;
    private final Function<TicketDto, Ticket> ticketDtoToTicketConverter;
    private final Map<CrudQueriesOperations, String> ticketQueriesProperties;

    /**
     * C-or
     *
     * @param databaseClient             the {@link DatabaseClient} used for
     *                                   connecting to the database for the Ticket
     *                                   objects
     * @param ticketRowMapper            the {@link TicketRowMapper} used for
     *                                   returning Ticket objects from the database
     * @param ticketDtoToTicketConverter the {@link TicketDtoToTicketConverter} used
     *                                   for converting {@link TicketDto} to
     *                                   {@link Ticket}
     * @param ticketQueriesProperties    the {@link Map} which are used for
     *                                   getting the right query CRUD database
     *                                   operations
     */
    public TicketRepository(final DatabaseClient databaseClient,
                            @Qualifier("ticketRowMapper") final TicketRowMapper ticketRowMapper,
                            @Qualifier("ticketDtoToTicketConverter") final Function<TicketDto, Ticket> ticketDtoToTicketConverter,
                            @Qualifier("queriesProperties") final Map<CrudQueriesOperations, String> ticketQueriesProperties) {

        this.databaseClient = requireNonNull(databaseClient);
        this.ticketRowMapper = requireNonNull(ticketRowMapper);
        this.ticketDtoToTicketConverter = requireNonNull(ticketDtoToTicketConverter);
        this.ticketQueriesProperties = requireNonNull(ticketQueriesProperties);
    }

    @Override
    public Mono<Ticket> save(final TicketDto ticketDto) {

        return saveTicket(ticketDto);
    }

    @Override
    public Mono<Ticket> edit(final TicketDto ticketDto) {

        return editTicket(ticketDto);
    }

    @Override
    public Mono<Ticket> findById(final UUID uuid) {

        return databaseClient.sql(ticketQueriesProperties.get(CrudQueriesOperations.GET_ID))
                .bind(0, uuid).map(ticketRowMapper).one();
    }

    @Override
    public Mono<Boolean> deleteById(final UUID uuid) {

        final Mono<Long> rows = databaseClient
                .sql(ticketQueriesProperties.get(CrudQueriesOperations.DELETE_ID)).bind(0, uuid).fetch()
                .rowsUpdated();

        return rows.map(this::rowsAffectedIsOne);
    }

    @Override
    public Mono<Boolean> existsById(final UUID uuid) {

        return findById(uuid).map(Objects::nonNull).defaultIfEmpty(false);
    }

    @Override
    public Flux<Ticket> findAll() {

        return databaseClient.sql(ticketQueriesProperties.get(CrudQueriesOperations.GET_ALL))
                .map(ticketRowMapper).all();
    }

    private Mono<Ticket> saveTicket(final TicketDto ticket) {

        final OffsetDateTime now = OffsetDateTime.now();
        final UUID uuid = ticket.uuid() != null ? ticket.uuid() : UUID.randomUUID();
        final SeatingInformation seatInformation = ticket.seatInformation();

        final Mono<Long> rowsAffected = databaseClient.sql(ticketQueriesProperties.get(CrudQueriesOperations.SAVE))
                .bind(0, uuid)
                .bind(1, now)
                .bind(2, now)
                .bind(3, ticket.eventID())
                .bind(4, ticket.ticketType())
                .bind(5, ticket.price())
                .bind(6, ticket.transferable())
                .bind(7, seatInformation.seat())
                .bind(8, seatInformation.section())
                .bind(9, ticket.used())
                .fetch()
                .rowsUpdated();

        return rowsAffected.filter(this::rowsAffectedIsOne)
                .map(rowNum -> ticketDtoToTicketConverter.apply(
                        TicketDto.from(ticket)
                                .uuid(uuid)
                                .createdAt(now)
                                .lastUpdated(now)
                                .build()));
    }

    private Mono<Ticket> editTicket(final TicketDto ticket) {

        final UUID uuid = ticket.uuid();
        final OffsetDateTime lastUpdated = OffsetDateTime.now();
        final SeatingInformation seatInformation = ticket.seatInformation();

        final Mono<Long> rowsAffected = databaseClient
                .sql(ticketQueriesProperties.get(CrudQueriesOperations.EDIT))
                .bind(0, lastUpdated)
                .bind(1, ticket.eventID())
                .bind(2, ticket.ticketType())
                .bind(3, ticket.price())
                .bind(4, ticket.transferable())
                .bind(5, seatInformation.seat())
                .bind(6, seatInformation.section())
                .bind(7, ticket.used())
                .bind(8, uuid)
                .fetch()
                .rowsUpdated();

        return rowsAffected.filter(this::rowsAffectedIsOne)
                .flatMap(rowNum -> findById(uuid))
                .map(AbstractDomainObject::getCreatedAt)
                .map(monoCreatedAt -> ticketDtoToTicketConverter.apply(
                        TicketDto.from(ticket)
                                .createdAt(monoCreatedAt)
                                .lastUpdated(lastUpdated)
                                .build()));
    }

    private boolean rowsAffectedIsOne(final Long x) {
        return x==1;
    }

}
