package io.github.evaggelos99.ems.ticket.service;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.SeatingInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.TicketType;
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

import java.time.Instant;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Component
public class TicketRepository implements ITicketRepository {

    private final DatabaseClient databaseClient;
    private final TicketRowMapper ticketRowMapper;
    private final Function<TicketDto, Ticket> ticketDtoToTicketConverter;
    private final Properties ticketQueriesProperties;

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
     * @param ticketQueriesProperties    the {@link Properties} which are used for
     *                                   getting the right query CRUD database
     *                                   operations
     */
    public TicketRepository(final DatabaseClient databaseClient,
                            @Qualifier("ticketRowMapper") final TicketRowMapper ticketRowMapper,
                            @Qualifier("ticketDtoToTicketConverter") final Function<TicketDto, Ticket> ticketDtoToTicketConverter,
                            @Qualifier("queriesProperties") final Properties ticketQueriesProperties) {

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

        return databaseClient.sql(ticketQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()))
                .bind(0, uuid).map(ticketRowMapper).one();
    }

    @Override
    public Mono<Boolean> deleteById(final UUID uuid) {

        final Mono<Long> rows = databaseClient
                .sql(ticketQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name())).bind(0, uuid).fetch()
                .rowsUpdated();

        return rows.map(this::rowsAffectedAreMoreThanOne);
    }

    @Override
    public Mono<Boolean> existsById(final UUID uuid) {

        return findById(uuid).map(Objects::nonNull);
    }

    @Override
    public Flux<Ticket> findAll() {

        return databaseClient.sql(ticketQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()))
                .map(ticketRowMapper).all();
    }

    private Mono<Ticket> saveTicket(final TicketDto ticket) {

        final UUID ticketUuid = ticket.uuid();
        final Instant now = Instant.now();
        final UUID uuid = ticketUuid != null ? ticketUuid : UUID.randomUUID();

        final UUID eventId = ticket.eventID();
        final TicketType ticketType = ticket.ticketType();
        final Integer price = ticket.price();
        final Boolean isTransferable = ticket.transferable();
        final SeatingInformation seatInformation = ticket.seatInformation();

        final Mono<Long> rowsAffected = databaseClient
                .sql(ticketQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name())).bind(0, uuid).bind(1, now)
                .bind(2, now).bind(3, eventId).bind(4, ticketType).bind(5, price).bind(6, isTransferable)
                .bind(7, seatInformation.seat()).bind(8, seatInformation.section()).fetch().rowsUpdated();

        return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).map(rowNum -> ticketDtoToTicketConverter
                .apply(new TicketDto(uuid, now, now, eventId, ticketType, price, isTransferable, seatInformation)));
    }

    private Mono<Ticket> editTicket(final TicketDto ticket) {

        final UUID uuid = ticket.uuid();
        final Instant updatedAt = Instant.now();

        final UUID eventId = ticket.eventID();
        final TicketType ticketType = ticket.ticketType();
        final Integer price = ticket.price();
        final Boolean isTransferable = ticket.transferable();
        final SeatingInformation seatInformation = ticket.seatInformation();

        final Mono<Long> rowsAffected = databaseClient
                .sql(ticketQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name())).bind(0, uuid)
                .bind(1, updatedAt).bind(2, eventId).bind(3, ticketType).bind(4, price).bind(5, isTransferable)
                .bind(6, seatInformation.seat()).bind(7, seatInformation.section()).bind(8, uuid).fetch().rowsUpdated();

        return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).flatMap(rowNum -> findById(uuid))
                .map(AbstractDomainObject::getCreatedAt)
                .map(monoCreatedAt -> ticketDtoToTicketConverter.apply(new TicketDto(uuid, monoCreatedAt, updatedAt,
                        eventId, ticketType, price, isTransferable, seatInformation)));
    }

    private boolean rowsAffectedAreMoreThanOne(final Long x) {
        return x >= 1;
    }

}
